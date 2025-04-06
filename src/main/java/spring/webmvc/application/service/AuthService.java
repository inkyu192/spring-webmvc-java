package spring.webmvc.application.service;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Member;
import spring.webmvc.domain.model.entity.MemberPermission;
import spring.webmvc.domain.model.entity.Permission;
import spring.webmvc.domain.model.entity.RolePermission;
import spring.webmvc.domain.repository.MemberRepository;
import spring.webmvc.domain.repository.TokenRepository;
import spring.webmvc.infrastructure.config.security.JwtTokenProvider;
import spring.webmvc.presentation.dto.request.MemberLoginRequest;
import spring.webmvc.presentation.dto.request.TokenRequest;
import spring.webmvc.presentation.dto.response.TokenResponse;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

	private final JwtTokenProvider jwtTokenProvider;
	private final TokenRepository tokenRepository;
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public TokenResponse login(MemberLoginRequest memberLoginRequest) {
		Member member = memberRepository.findByAccount(memberLoginRequest.account())
			.filter(it -> passwordEncoder.matches(memberLoginRequest.password(), it.getPassword()))
			.orElseThrow(() -> new BadCredentialsException("잘못된 아이디 또는 비밀번호입니다."));

		String accessToken = jwtTokenProvider.createAccessToken(member.getId(), getPermissions(member));
		String refreshToken = jwtTokenProvider.createRefreshToken();

		tokenRepository.save(member.getId(), refreshToken);

		return new TokenResponse(accessToken, refreshToken);
	}

	public TokenResponse refreshToken(TokenRequest tokenRequest) {
		Long memberId = extractMemberId(tokenRequest.accessToken());
		jwtTokenProvider.validateRefreshToken(tokenRequest.refreshToken());

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException(Member.class, memberId));

		String refreshToken = tokenRepository.findByMemberId(memberId)
			.filter(it -> tokenRequest.refreshToken().equals(it))
			.orElseThrow(() -> new BadCredentialsException("유효하지 않은 인증 정보입니다. 다시 로그인해 주세요."));

		return new TokenResponse(
			jwtTokenProvider.createAccessToken(
				member.getId(),
				getPermissions(member)
			),
			refreshToken
		);
	}

	private Long extractMemberId(String accessToken) {
		Claims claims;

		try {
			claims = jwtTokenProvider.parseAccessToken(accessToken);
		} catch (ExpiredJwtException e) {
			claims = e.getClaims();
		}

		return Long.valueOf(String.valueOf(claims.get("memberId")));
	}

	private List<String> getPermissions(Member member) {
		return Stream.concat(
				member.getMemberRoles().stream()
					.flatMap(memberRole -> memberRole.getRole()
						.getRolePermissions().stream()
						.map(RolePermission::getPermission)
					),
				member.getMemberPermissions().stream()
					.map(MemberPermission::getPermission)
			)
			.map(Permission::getName)
			.distinct()
			.toList();
	}
}
