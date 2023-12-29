package com.webmvc.javaapi.service;

import com.webmvc.javaapi.config.security.JwtTokenProvider;
import com.webmvc.javaapi.constant.ApiResponseCode;
import com.webmvc.javaapi.domain.Token;
import com.webmvc.javaapi.dto.request.LoginRequest;
import com.webmvc.javaapi.dto.request.TokenRequest;
import com.webmvc.javaapi.dto.response.TokenResponse;
import com.webmvc.javaapi.exception.CommonException;
import com.webmvc.javaapi.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    @Transactional
    public TokenResponse login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.account(), loginRequest.password());

        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (BadCredentialsException e) {
            throw new CommonException(ApiResponseCode.BAD_CREDENTIALS);
        }

        String accessToken = jwtTokenProvider.createAccessToken(
                authentication.getName(),
                authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(","))
        );
        String refreshToken = jwtTokenProvider.createRefreshToken();

        tokenRepository.save(Token.create(authentication.getName(), refreshToken));

        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse refresh(TokenRequest tokenRequest) {
        Claims claims;

        try {
            claims = jwtTokenProvider.parseAccessToken(tokenRequest.accessToken());
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        }

        String account = (String) claims.get("account");
        String authorities = claims.get("authorities").toString();

        String refreshToken = tokenRepository.findById(account)
                .map(Token::getRefreshToken)
                .orElseThrow(() -> new CommonException(ApiResponseCode.BAD_CREDENTIALS));

        if (!tokenRequest.refreshToken().equals(refreshToken)) {
            throw new CommonException(ApiResponseCode.BAD_CREDENTIALS);
        }

        return new TokenResponse(jwtTokenProvider.createAccessToken(account, authorities));
    }
}
