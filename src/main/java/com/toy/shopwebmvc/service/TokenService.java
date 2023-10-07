package com.toy.shopwebmvc.service;

import com.toy.shopwebmvc.constant.ApiResponseCode;
import com.toy.shopwebmvc.domain.Token;
import com.toy.shopwebmvc.dto.request.TokenRequest;
import com.toy.shopwebmvc.dto.response.TokenResponse;
import com.toy.shopwebmvc.exception.CommonException;
import com.toy.shopwebmvc.repository.TokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TokenService {

    private final Key accessTokenKey;
    private final long accessTokenExpirationTime;
    private final Key refreshTokenKey;
    private final long refreshTokenExpirationTime;
    private final TokenRepository tokenRepository;

    public TokenService(
            @Value("${jwt.access-token.key}") String accessTokenKey,
            @Value("${jwt.access-token.expiration-time}") long accessTokenExpirationTime,
            @Value("${jwt.refresh-token.key}") String refreshTokenKey,
            @Value("${jwt.refresh-token.expiration-time}") long refreshTokenExpirationTime,
            TokenRepository tokenRepository
    ) {
        this.accessTokenKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessTokenKey));
        this.accessTokenExpirationTime = accessTokenExpirationTime * 60 * 1000;
        this.refreshTokenKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshTokenKey));
        this.refreshTokenExpirationTime = refreshTokenExpirationTime * 60 * 1000;
        this.tokenRepository = tokenRepository;
    }

    public TokenResponse refresh(TokenRequest tokenRequest) {
        String accessToken = tokenRequest.accessToken();
        String refreshToken = tokenRequest.refreshToken();

        Claims claims;

        try {
            claims = parseClaims(accessToken);
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        }

        try {
            parseClaims(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new CommonException(ApiResponseCode.LOGIN_REQUIRED);
        }

        String account = String.valueOf(claims.get("account"));

        String findRefreshToken = tokenRepository.findById(account)
                .map(Token::getRefreshToken)
                .orElseThrow(() -> new CommonException(ApiResponseCode.BAD_REQUEST));

        if (!refreshToken.equals(findRefreshToken)) {
            throw new CommonException(ApiResponseCode.BAD_REQUEST);
        }

        return new TokenResponse(createAccessToken(SecurityContextHolder.getContext().getAuthentication()));
    }

    public String createAccessToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .claim("account", authentication.getName())
                .claim("authorities", authorities)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + accessTokenExpirationTime))
                .signWith(accessTokenKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Transactional
    public String createRefreshToken(Authentication authentication) {
        String refreshToken = Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + refreshTokenExpirationTime))
                .signWith(refreshTokenKey, SignatureAlgorithm.HS256)
                .compact();

        tokenRepository.save(Token.createToken(authentication.getName(), refreshToken));

        return refreshToken;
    }

    public String getAccessToken(HttpServletRequest request) {
        String accessToken = null;
        String token = request.getHeader("Authorization");

        if (StringUtils.hasText(token) && token.startsWith("Bearer")) {
            accessToken = token.replace("Bearer ", "");
        }

        return accessToken;
    }

    public Claims parseClaims(String token) {
        Claims claims;

        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(accessTokenKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException e) {
            throw new SecurityException("잘못된 토큰", e);
        }  catch (MalformedJwtException e) {
            throw new MalformedJwtException("잘못된 토큰", e);
        }  catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtException("지원되지 않는 토큰", e);
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "만료된 토큰", e);
        }

        return claims;
    }
}
