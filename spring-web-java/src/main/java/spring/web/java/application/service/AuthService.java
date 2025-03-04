package spring.web.java.application.service;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import spring.web.java.domain.model.entity.Member;
import spring.web.java.domain.model.entity.MemberPermission;
import spring.web.java.domain.model.entity.Permission;
import spring.web.java.domain.model.entity.RolePermission;
import spring.web.java.domain.model.entity.Token;
import spring.web.java.domain.repository.MemberRepository;
import spring.web.java.domain.repository.TokenRepository;
import spring.web.java.infrastructure.config.security.JwtTokenProvider;
import spring.web.java.presentation.dto.request.MemberLoginRequest;
import spring.web.java.presentation.dto.request.TokenRequest;
import spring.web.java.presentation.dto.response.TokenResponse;
import spring.web.java.presentation.exception.EntityNotFoundException;

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

		tokenRepository.save(Token.create(member.getId(), refreshToken));

		return new TokenResponse(accessToken, refreshToken);
	}

	public TokenResponse refreshToken(TokenRequest tokenRequest) {
		Long memberId = extractMemberId(tokenRequest.accessToken());
		jwtTokenProvider.validateRefreshToken(tokenRequest.refreshToken());

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException(Member.class, memberId));

		String refreshToken = tokenRepository.findById(memberId)
			.map(Token::getRefreshToken)
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
