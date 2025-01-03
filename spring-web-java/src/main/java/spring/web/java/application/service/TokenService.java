package spring.web.java.application.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import spring.web.java.application.port.in.TokenServicePort;
import spring.web.java.application.port.out.MemberRepositoryPort;
import spring.web.java.application.port.out.TokenRepositoryPort;
import spring.web.java.common.JwtTokenProvider;
import spring.web.java.common.ResponseMessage;
import spring.web.java.domain.Member;
import spring.web.java.domain.Token;
import spring.web.java.dto.request.TokenRequest;
import spring.web.java.dto.response.TokenResponse;
import spring.web.java.infrastructure.configuration.exception.DomainException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TokenService implements TokenServicePort {

	private final JwtTokenProvider jwtTokenProvider;
	private final TokenRepositoryPort tokenRepository;
	private final MemberRepositoryPort memberRepository;

	@Override
	public TokenResponse reissue(TokenRequest tokenRequest) {
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
			.orElseThrow(() -> new DomainException(ResponseMessage.AUTHENTICATION_FAILED, HttpStatus.UNAUTHORIZED));

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new DomainException(ResponseMessage.AUTHENTICATION_FAILED, HttpStatus.UNAUTHORIZED));

		return new TokenResponse(jwtTokenProvider.createAccessToken(member.getId(), member.getRole()), refreshToken);
	}
}
