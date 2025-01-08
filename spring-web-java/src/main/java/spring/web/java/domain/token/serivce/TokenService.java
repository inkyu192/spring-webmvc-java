package spring.web.java.domain.token.serivce;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import spring.web.java.domain.member.Member;
import spring.web.java.domain.member.repository.MemberRepository;
import spring.web.java.domain.token.Token;
import spring.web.java.domain.token.repository.TokenRepository;
import spring.web.java.domain.token.dto.TokenRequest;
import spring.web.java.domain.token.dto.TokenResponse;
import spring.web.java.global.common.JwtTokenProvider;
import spring.web.java.global.common.ResponseMessage;
import spring.web.java.global.exception.DomainException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TokenService {

	private final JwtTokenProvider jwtTokenProvider;
	private final TokenRepository tokenRepository;
	private final MemberRepository memberRepository;

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
