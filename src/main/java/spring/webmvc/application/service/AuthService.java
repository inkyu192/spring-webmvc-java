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
import spring.webmvc.application.dto.result.TokenResult;
import spring.webmvc.domain.cache.ValueCache;
import spring.webmvc.domain.model.entity.Member;
import spring.webmvc.domain.model.entity.MemberPermission;
import spring.webmvc.domain.model.entity.Permission;
import spring.webmvc.domain.model.entity.RolePermission;
import spring.webmvc.domain.model.vo.Email;
import spring.webmvc.domain.repository.MemberRepository;
import spring.webmvc.domain.cache.CacheKey;
import spring.webmvc.infrastructure.security.JwtProvider;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

	private final JwtProvider jwtProvider;
	private final ValueCache valueCache;
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public TokenResult login(String email, String password) {
		Member member = memberRepository.findByEmail(Email.create(email))
			.filter(it -> passwordEncoder.matches(password, it.getPassword()))
			.orElseThrow(() -> new BadCredentialsException("잘못된 아이디 또는 비밀번호입니다."));

		String accessToken = jwtProvider.createAccessToken(member.getId(), getPermissions(member));
		String refreshToken = jwtProvider.createRefreshToken();

		String key = CacheKey.REFRESH_TOKEN.generate(member.getId());
		valueCache.set(key, refreshToken, CacheKey.REFRESH_TOKEN.getTimeout());

		return new TokenResult(accessToken, refreshToken);
	}

	public TokenResult refreshToken(String accessToken, String refreshToken) {
		Long memberId = extractMemberId(accessToken);
		jwtProvider.validateRefreshToken(refreshToken);

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException(Member.class, memberId));

		if (!valueCache.get(CacheKey.REFRESH_TOKEN.generate(memberId)).equals(refreshToken)) {
			throw new BadCredentialsException("유효하지 않은 인증 정보입니다. 다시 로그인해 주세요.");
		}

		return new TokenResult(jwtProvider.createAccessToken(memberId, getPermissions(member)), refreshToken);
	}

	private Long extractMemberId(String accessToken) {
		Claims claims;

		try {
			claims = jwtProvider.parseAccessToken(accessToken);
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
