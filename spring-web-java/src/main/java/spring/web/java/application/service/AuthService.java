package spring.web.java.application.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import spring.web.java.domain.model.entity.Member;
import spring.web.java.domain.model.entity.Token;
import spring.web.java.domain.repository.MemberRepository;
import spring.web.java.domain.repository.TokenRepository;
import spring.web.java.infrastructure.security.JwtTokenProvider;
import spring.web.java.presentation.dto.request.MemberLoginRequest;
import spring.web.java.presentation.dto.request.TokenRequest;
import spring.web.java.presentation.dto.response.TokenResponse;
import spring.web.java.presentation.exception.BaseException;
import spring.web.java.presentation.exception.ResponseMessage;

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
			.orElseThrow(() -> new BaseException(ResponseMessage.AUTHENTICATION_FAILED, HttpStatus.UNAUTHORIZED));

		if (!passwordEncoder.matches(memberLoginRequest.password(), member.getPassword())) {
			throw new BaseException(ResponseMessage.AUTHENTICATION_FAILED, HttpStatus.UNAUTHORIZED);
		}

		String accessToken = jwtTokenProvider.createAccessToken(member.getId(), member.getRole());
		String refreshToken = jwtTokenProvider.createRefreshToken();

		tokenRepository.save(Token.create(member.getId(), refreshToken));

		return new TokenResponse(accessToken, refreshToken);
	}

	public TokenResponse refreshToken(TokenRequest tokenRequest) {
		Claims claims;

		try {
			claims = jwtTokenProvider.parseAccessToken(tokenRequest.accessToken());
		} catch (ExpiredJwtException e) {
			claims = e.getClaims();
		}

		jwtTokenProvider.parseRefreshToken(tokenRequest.refreshToken());

		Long memberId = Long.valueOf(String.valueOf(claims.get("memberId")));

		String refreshToken = tokenRepository.findById(memberId)
			.map(Token::getRefreshToken)
			.filter(token -> tokenRequest.refreshToken().equals(token))
			.orElseThrow(() -> new BaseException(ResponseMessage.AUTHENTICATION_FAILED, HttpStatus.UNAUTHORIZED));

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new BaseException(ResponseMessage.AUTHENTICATION_FAILED, HttpStatus.UNAUTHORIZED));

		return new TokenResponse(jwtTokenProvider.createAccessToken(member.getId(), member.getRole()), refreshToken);
	}
}
