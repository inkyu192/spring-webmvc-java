package com.toy.shopwebmvc.service;

import com.toy.shopwebmvc.repository.TokenRepository;
import com.toy.shopwebmvc.common.UserDetailsImpl;
import com.toy.shopwebmvc.domain.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class TokenService {

    private final Key accessTokenKey;
    private final long accessTokenExpirationTime;
    private final Key refreshTokenKey;
    private final long refreshTokenExpirationTime;
    private final TokenRepository tokenRepository;

    public TokenService(@Value("${jwt.access-token.key}") String accessTokenKey,
                        @Value("${jwt.access-token.expiration-time}") long accessTokenExpirationTime,
                        @Value("${jwt.refresh-token.key}") String refreshTokenKey,
                        @Value("${jwt.refresh-token.expiration-time}") long refreshTokenExpirationTime,
                        TokenRepository tokenRepository) {
        this.accessTokenKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessTokenKey));
        this.accessTokenExpirationTime = accessTokenExpirationTime * 60 * 1000;
        this.refreshTokenKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshTokenKey));
        this.refreshTokenExpirationTime = refreshTokenExpirationTime * 60 * 1000;
        this.tokenRepository = tokenRepository;
    }

    public String createAccessToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .claim("account", authentication.getName())
                .claim("authorities", authorities)
                .setExpiration(new Date(new Date().getTime() + refreshTokenExpirationTime))
                .signWith(accessTokenKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(Authentication authentication) {
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(new Date().getTime() + accessTokenExpirationTime))
                .signWith(refreshTokenKey, SignatureAlgorithm.HS256)
                .compact();

        tokenRepository.save(Token.createToken(authentication.getName(), refreshToken));

        return refreshToken;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(accessTokenKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        UserDetails userDetails = new UserDetailsImpl(claims);

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
