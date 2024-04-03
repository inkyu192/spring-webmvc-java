package spring.web.java.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.web.java.config.security.JwtTokenProvider;
import spring.web.java.constant.ApiResponseCode;
import spring.web.java.domain.Member;
import spring.web.java.domain.Token;
import spring.web.java.dto.request.TokenRequest;
import spring.web.java.dto.response.TokenResponse;
import spring.web.java.exception.CommonException;
import spring.web.java.repository.MemberRepository;
import spring.web.java.repository.TokenRepository;

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
                .orElseThrow(() -> new CommonException(ApiResponseCode.BAD_CREDENTIALS));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CommonException(ApiResponseCode.BAD_CREDENTIALS));

        return new TokenResponse(jwtTokenProvider.createAccessToken(member.getId(), member.getRole()), refreshToken);
    }
}
