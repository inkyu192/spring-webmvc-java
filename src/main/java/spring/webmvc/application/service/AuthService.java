package spring.webmvc.application.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import spring.webmvc.application.dto.command.JoinVerifyConfirmCommand;
import spring.webmvc.application.dto.command.JoinVerifyRequestCommand;
import spring.webmvc.application.dto.command.PasswordResetConfirmCommand;
import spring.webmvc.application.dto.command.PasswordResetRequestCommand;
import spring.webmvc.application.dto.command.RefreshTokenCommand;
import spring.webmvc.application.dto.command.SignInCommand;
import spring.webmvc.application.dto.command.SignUpCommand;
import spring.webmvc.application.dto.result.TokenResult;
import spring.webmvc.application.event.SendPasswordResetEmailEvent;
import spring.webmvc.application.event.SendVerifyEmailEvent;
import spring.webmvc.domain.model.entity.User;
import spring.webmvc.domain.model.entity.UserCredential;
import spring.webmvc.domain.model.vo.Email;
import spring.webmvc.domain.repository.PermissionRepository;
import spring.webmvc.domain.repository.RoleRepository;
import spring.webmvc.domain.repository.UserCredentialRepository;
import spring.webmvc.domain.repository.UserRepository;
import spring.webmvc.domain.repository.cache.AuthCacheRepository;
import spring.webmvc.domain.repository.cache.TokenCacheRepository;
import spring.webmvc.infrastructure.exception.DuplicateEntityException;
import spring.webmvc.infrastructure.exception.NotFoundEntityException;
import spring.webmvc.infrastructure.external.s3.FileType;
import spring.webmvc.infrastructure.external.s3.S3Service;
import spring.webmvc.infrastructure.security.JwtProvider;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
	private final JwtProvider jwtProvider;
	private final TokenCacheRepository tokenCacheRepository;
	private final UserRepository userRepository;
	private final UserCredentialRepository userCredentialRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthCacheRepository authCacheRepository;
	private final ApplicationEventPublisher eventPublisher;
	private final RoleRepository roleRepository;
	private final PermissionRepository permissionRepository;
	private final S3Service s3Service;

	@Transactional
	public User signUp(SignUpCommand command) {
		if (userCredentialRepository.existsByEmail(command.email())) {
			throw new DuplicateEntityException(UserCredential.class, command.email().getValue());
		}

		if (userRepository.existsByPhone(command.phone())) {
			throw new DuplicateEntityException(User.class, command.phone().getValue());
		}

		User user = User.create(
			command.name(),
			command.phone(),
			command.gender(),
			command.birthday(),
			command.profileImageKey()
		);

		roleRepository.findAllById(command.roleIds()).forEach(user::addUserRole);
		permissionRepository.findAllById(command.permissionIds()).forEach(user::addUserPermission);

		userRepository.save(user);

		if (command.profileImageKey() != null) {
			s3Service.copyObject(command.profileImageKey(), FileType.PROFILE, user.getId());

			user.updateProfileImage(command.profileImageKey());
		}

		UserCredential userCredential = UserCredential.create(
			user,
			command.email(),
			passwordEncoder.encode(command.password())
		);

		userCredentialRepository.save(userCredential);

		eventPublisher.publishEvent(new SendVerifyEmailEvent(command.email()));

		return user;
	}

	public TokenResult signIn(SignInCommand command) {
		UserCredential userCredential = userCredentialRepository.findByEmail(command.email())
			.orElseThrow(() -> new NotFoundEntityException(UserCredential.class, command.email().getValue()));

		if (!passwordEncoder.matches(command.password(), userCredential.getPassword())) {
			throw new BadCredentialsException("유효하지 않은 인증 정보입니다.");
		}

		if (!userCredential.isVerified()) {
			throw new BadCredentialsException("이메일 인증이 필요합니다.");
		}

		User user = userCredential.getUser();

		String accessToken = jwtProvider.createAccessToken(user.getId(), user.getPermissionNames());
		String refreshToken = jwtProvider.createRefreshToken();

		tokenCacheRepository.addRefreshToken(user.getId(), refreshToken);

		return new TokenResult(accessToken, refreshToken);
	}

	public TokenResult refreshToken(RefreshTokenCommand command) {
		jwtProvider.parseRefreshToken(command.refreshToken());

		Long userId = extractUserId(command.accessToken());

		String refreshToken = tokenCacheRepository.getRefreshToken(userId, command.refreshToken());

		if (refreshToken == null) {
			throw new BadCredentialsException("유효하지 않은 인증 정보입니다.");
		}

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundEntityException(User.class, userId));

		tokenCacheRepository.removeRefreshToken(userId, command.refreshToken());

		String newRefreshToken = jwtProvider.createRefreshToken();
		tokenCacheRepository.addRefreshToken(userId, newRefreshToken);

		return new TokenResult(jwtProvider.createAccessToken(user.getId(), user.getPermissionNames()), newRefreshToken);
	}

	private Long extractUserId(String accessToken) {
		Claims claims;

		try {
			claims = jwtProvider.parseAccessToken(accessToken);
		} catch (ExpiredJwtException e) {
			claims = e.getClaims();
		}

		return Long.parseLong(claims.get("userId").toString());
	}

	public void requestJoinVerify(JoinVerifyRequestCommand command) {
		eventPublisher.publishEvent(new SendVerifyEmailEvent(command.email()));
	}

	@Transactional
	public void confirmJoinVerify(JoinVerifyConfirmCommand command) {
		String email = authCacheRepository.getJoinVerifyToken(command.token());
		if (email == null) {
			throw new BadCredentialsException("유효하지 않은 인증 정보입니다.");
		}

		UserCredential userCredential = userCredentialRepository.findByEmail(Email.create(email))
			.orElseThrow(() -> new NotFoundEntityException(UserCredential.class, email));

		userCredential.verify();

		authCacheRepository.deleteJoinVerifyToken(command.token());
	}

	public void requestPasswordReset(PasswordResetRequestCommand command) {
		userCredentialRepository.findByEmail(command.email())
			.orElseThrow(() -> new NotFoundEntityException(UserCredential.class, command.email().getValue()));

		eventPublisher.publishEvent(new SendPasswordResetEmailEvent(command.email()));
	}

	@Transactional
	public void confirmPasswordReset(PasswordResetConfirmCommand command) {
		String email = authCacheRepository.getPasswordResetToken(command.token());

		if (email == null) {
			throw new BadCredentialsException("유효하지 않은 인증 정보입니다.");
		}

		UserCredential userCredential = userCredentialRepository.findByEmail(Email.create(email))
			.orElseThrow(() -> new NotFoundEntityException(UserCredential.class, email));

		userCredential.updatePassword(passwordEncoder.encode(command.newPassword()));

		authCacheRepository.deletePasswordResetToken(command.token());
	}
}
