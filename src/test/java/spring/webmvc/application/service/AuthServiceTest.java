package spring.webmvc.application.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
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
import spring.webmvc.domain.model.enums.Gender;
import spring.webmvc.domain.model.vo.Email;
import spring.webmvc.domain.model.vo.Phone;
import spring.webmvc.domain.repository.PermissionRepository;
import spring.webmvc.domain.repository.RoleRepository;
import spring.webmvc.domain.repository.UserCredentialRepository;
import spring.webmvc.domain.repository.UserRepository;
import spring.webmvc.domain.repository.cache.AuthCacheRepository;
import spring.webmvc.domain.repository.cache.TokenCacheRepository;
import spring.webmvc.infrastructure.exception.DuplicateEntityException;
import spring.webmvc.infrastructure.exception.InvalidCredentialsException;
import spring.webmvc.infrastructure.exception.NotFoundEntityException;
import spring.webmvc.infrastructure.external.s3.S3Service;
import spring.webmvc.infrastructure.security.JwtProvider;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Mock
	private JwtProvider jwtProvider;

	@Mock
	private TokenCacheRepository tokenCacheRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private UserCredentialRepository userCredentialRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private AuthCacheRepository authCacheRepository;

	@Mock
	private ApplicationEventPublisher eventPublisher;

	@Mock
	private RoleRepository roleRepository;

	@Mock
	private PermissionRepository permissionRepository;

	@Mock
	private S3Service s3Service;

	private AuthService authService;

	private User user;
	private UserCredential userCredential;
	private Email email;
	private final Long userId = 1L;
	private final String accessToken = "accessToken";
	private final String refreshToken = "refreshToken";

	@BeforeEach
	void setUp() {
		authService = new AuthService(
			jwtProvider,
			tokenCacheRepository,
			userRepository,
			userCredentialRepository,
			passwordEncoder,
			authCacheRepository,
			eventPublisher,
			roleRepository,
			permissionRepository,
			s3Service
		);

		email = Email.create("test@example.com");
		user = spy(User.create(
			"홍길동",
			Phone.create("010-1234-5678"),
			Gender.MALE,
			LocalDate.of(1990, 1, 1),
			null
		));

		userCredential = spy(UserCredential.create(
			user,
			email,
			"encodedPassword"
		));
	}

	@Test
	@DisplayName("회원가입 성공")
	void signUp() {
		SignUpCommand command = new SignUpCommand(
			email,
			"password123",
			"홍길동",
			Phone.create("010-1234-5678"),
			Gender.MALE,
			LocalDate.of(1990, 1, 1),
			null,
			List.of(),
			List.of()
		);

		when(userCredentialRepository.existsByEmail(email)).thenReturn(false);
		when(userRepository.existsByPhone(any())).thenReturn(false);
		when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
		when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
		when(roleRepository.findAllById(any())).thenReturn(List.of());
		when(permissionRepository.findAllById(any())).thenReturn(List.of());

		User result = authService.signUp(command);

		assertThat(result).isNotNull();
		verify(userCredentialRepository).existsByEmail(email);
		verify(userRepository).existsByPhone(any());
		verify(userRepository).save(any());
		verify(userCredentialRepository).save(any());
	}

	@Test
	@DisplayName("프로필 이미지 없이 회원가입 시 S3 copyObject 호출되지 않음")
	void signUpWithoutProfileImage() {
		SignUpCommand command = new SignUpCommand(
			email,
			"password123",
			"홍길동",
			Phone.create("010-1234-5678"),
			Gender.MALE,
			LocalDate.of(1990, 1, 1),
			null,
			List.of(),
			List.of()
		);

		when(userCredentialRepository.existsByEmail(email)).thenReturn(false);
		when(userRepository.existsByPhone(any())).thenReturn(false);
		when(roleRepository.findAllById(any())).thenReturn(List.of());
		when(permissionRepository.findAllById(any())).thenReturn(List.of());
		when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
		when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		User result = authService.signUp(command);

		assertThat(result).isNotNull();
		verify(s3Service, never()).copyObject(any(), any(), any());
	}

	@Test
	@DisplayName("중복된 이메일로 회원가입 시 DuplicateEntityException 발생")
	void signUpWithDuplicateEmail() {
		SignUpCommand command = new SignUpCommand(
			email,
			"password123",
			"홍길동",
			Phone.create("010-1234-5678"),
			Gender.MALE,
			LocalDate.of(1990, 1, 1),
			null,
			List.of(),
			List.of()
		);

		when(userCredentialRepository.existsByEmail(email)).thenReturn(true);

		assertThatThrownBy(() -> authService.signUp(command))
			.isInstanceOf(DuplicateEntityException.class);
	}

	@Test
	@DisplayName("중복된 번호로 회원가입 시 DuplicateEntityException 발생")
	void signUpWithDuplicatePhone() {
		Phone phone = Phone.create("010-1234-5678");
		SignUpCommand command = new SignUpCommand(
			email,
			"password123",
			"홍길동",
			phone,
			Gender.MALE,
			LocalDate.of(1990, 1, 1),
			null,
			List.of(),
			List.of()
		);

		when(userCredentialRepository.existsByEmail(email)).thenReturn(false);
		when(userRepository.existsByPhone(phone)).thenReturn(true);

		assertThatThrownBy(() -> authService.signUp(command))
			.isInstanceOf(DuplicateEntityException.class);
	}

	@Test
	@DisplayName("로그인 성공")
	void signIn() {
		SignInCommand command = new SignInCommand(email, "password123");

		doReturn(userId).when(user).getId();
		doReturn(Collections.emptyList()).when(user).getPermissionNames();
		userCredential.verify();

		when(userCredentialRepository.findByEmail(email)).thenReturn(Optional.of(userCredential));
		when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
		when(jwtProvider.createAccessToken(userId, Collections.emptyList())).thenReturn(accessToken);
		when(jwtProvider.createRefreshToken()).thenReturn(refreshToken);
		doNothing().when(tokenCacheRepository).addRefreshToken(userId, refreshToken);

		TokenResult result = authService.signIn(command);

		assertThat(result.accessToken()).isEqualTo(accessToken);
		assertThat(result.refreshToken()).isEqualTo(refreshToken);
	}

	@Test
	@DisplayName("존재하지 않는 이메일로 로그인 시 NotFoundEntityException 발생")
	void signInWithNonExistentEmail() {
		SignInCommand command = new SignInCommand(email, "password123");

		when(userCredentialRepository.findByEmail(email)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> authService.signIn(command))
			.isInstanceOf(NotFoundEntityException.class);
	}

	@Test
	@DisplayName("잘못된 비밀번호로 로그인 시 InvalidCredentialsException 발생")
	void signInWithWrongPassword() {
		SignInCommand command = new SignInCommand(email, "wrongPassword");

		when(userCredentialRepository.findByEmail(email)).thenReturn(Optional.of(userCredential));
		when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

		assertThatThrownBy(() -> authService.signIn(command))
			.isInstanceOf(InvalidCredentialsException.class);
	}

	@Test
	@DisplayName("토큰 갱신 성공")
	void refreshToken() {
		RefreshTokenCommand command = new RefreshTokenCommand(accessToken, refreshToken);
		Claims claims = new DefaultClaims(Map.of("userId", userId));
		String newRefreshToken = "newRefreshToken";

		doReturn(userId).when(user).getId();
		doReturn(Collections.emptyList()).when(user).getPermissionNames();

		when(jwtProvider.parseRefreshToken(refreshToken)).thenReturn(mock(Claims.class));
		when(jwtProvider.parseAccessToken(accessToken)).thenReturn(claims);
		when(tokenCacheRepository.getRefreshToken(userId, refreshToken)).thenReturn(refreshToken);
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(jwtProvider.createAccessToken(userId, Collections.emptyList())).thenReturn("newAccessToken");
		when(jwtProvider.createRefreshToken()).thenReturn(newRefreshToken);
		doNothing().when(tokenCacheRepository).removeRefreshToken(userId, refreshToken);
		doNothing().when(tokenCacheRepository).addRefreshToken(userId, newRefreshToken);

		TokenResult result = authService.refreshToken(command);

		assertThat(result.accessToken()).isEqualTo("newAccessToken");
		assertThat(result.refreshToken()).isEqualTo(newRefreshToken);
		verify(tokenCacheRepository).removeRefreshToken(userId, refreshToken);
		verify(tokenCacheRepository).addRefreshToken(userId, newRefreshToken);
	}

	@Test
	@DisplayName("유효하지 않은 refresh token으로 갱신 시 InvalidCredentialsException 발생")
	void refreshTokenWithInvalidToken() {
		RefreshTokenCommand command = new RefreshTokenCommand(accessToken, "invalidRefreshToken");
		Claims claims = new DefaultClaims(Map.of("userId", userId));

		when(jwtProvider.parseRefreshToken("invalidRefreshToken")).thenReturn(mock(Claims.class));
		when(jwtProvider.parseAccessToken(accessToken)).thenReturn(claims);
		when(tokenCacheRepository.getRefreshToken(userId, "invalidRefreshToken")).thenReturn(null);

		assertThatThrownBy(() -> authService.refreshToken(command))
			.isInstanceOf(InvalidCredentialsException.class);
	}

	@Test
	@DisplayName("비밀번호 재설정 확인 성공")
	void confirmPasswordReset() {
		String token = "resetToken";
		String newPassword = "newPassword123";
		String encodedPassword = "encodedNewPassword";
		PasswordResetConfirmCommand command = new PasswordResetConfirmCommand(token, newPassword);

		when(authCacheRepository.getPasswordResetToken(token)).thenReturn(email.getValue());
		when(userCredentialRepository.findByEmail(any())).thenReturn(Optional.of(userCredential));
		when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);
		doNothing().when(authCacheRepository).deletePasswordResetToken(token);

		authService.confirmPasswordReset(command);

		verify(userCredential).updatePassword(encodedPassword);
	}

	@Test
	@DisplayName("유효하지 않은 토큰으로 비밀번호 재설정 확인 시 InvalidCredentialsException 발생")
	void confirmPasswordResetWithInvalidToken() {
		String token = "invalidToken";
		PasswordResetConfirmCommand command = new PasswordResetConfirmCommand(token, "newPassword123");

		when(authCacheRepository.getPasswordResetToken(token)).thenReturn(null);

		assertThatThrownBy(() -> authService.confirmPasswordReset(command))
			.isInstanceOf(InvalidCredentialsException.class);
	}

	@Test
	@DisplayName("이메일 인증 안된 계정으로 로그인 시 InvalidCredentialsException 발생")
	void signInWithUnverifiedEmail() {
		SignInCommand command = new SignInCommand(email, "password123");
		UserCredential unverifiedCredential = spy(UserCredential.create(user, email, "encodedPassword"));

		when(userCredentialRepository.findByEmail(email)).thenReturn(Optional.of(unverifiedCredential));
		when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);

		assertThatThrownBy(() -> authService.signIn(command))
			.isInstanceOf(InvalidCredentialsException.class);
	}

	@Test
	@DisplayName("회원가입 인증 요청")
	void requestJoinVerify() {
		JoinVerifyRequestCommand command = new JoinVerifyRequestCommand(email);

		authService.requestJoinVerify(command);

		verify(eventPublisher).publishEvent(any(SendVerifyEmailEvent.class));
	}

	@Test
	@DisplayName("회원가입 인증 확인 성공")
	void confirmJoinVerify() {
		String token = "verifyToken";
		JoinVerifyConfirmCommand command = new JoinVerifyConfirmCommand(token);
		UserCredential unverifiedCredential = spy(UserCredential.create(user, email, "encodedPassword"));

		when(authCacheRepository.getJoinVerifyToken(token)).thenReturn(email.getValue());
		when(userCredentialRepository.findByEmail(any())).thenReturn(Optional.of(unverifiedCredential));
		doNothing().when(authCacheRepository).deleteJoinVerifyToken(token);

		authService.confirmJoinVerify(command);

		assertThat(unverifiedCredential.isVerified()).isTrue();
	}

	@Test
	@DisplayName("유효하지 않은 토큰으로 회원가입 인증 확인 시 InvalidCredentialsException 발생")
	void confirmJoinVerifyWithInvalidToken() {
		String token = "invalidToken";
		JoinVerifyConfirmCommand command = new JoinVerifyConfirmCommand(token);

		when(authCacheRepository.getJoinVerifyToken(token)).thenReturn(null);

		assertThatThrownBy(() -> authService.confirmJoinVerify(command))
			.isInstanceOf(InvalidCredentialsException.class);
	}

	@Test
	@DisplayName("비밀번호 재설정 요청")
	void requestPasswordReset() {
		PasswordResetRequestCommand command = new PasswordResetRequestCommand(email);

		when(userCredentialRepository.findByEmail(email)).thenReturn(Optional.of(userCredential));

		authService.requestPasswordReset(command);

		verify(userCredentialRepository).findByEmail(email);
		verify(eventPublisher).publishEvent(any(SendPasswordResetEmailEvent.class));
	}

	@Test
	@DisplayName("존재하지 않는 이메일로 비밀번호 재설정 요청 시 NotFoundEntityException 발생")
	void requestPasswordResetWithNonExistentEmail() {
		PasswordResetRequestCommand command = new PasswordResetRequestCommand(email);

		when(userCredentialRepository.findByEmail(email)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> authService.requestPasswordReset(command))
			.isInstanceOf(NotFoundEntityException.class);
	}
}
