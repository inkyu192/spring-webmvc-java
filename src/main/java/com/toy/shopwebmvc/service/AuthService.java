package com.toy.shopwebmvc.service;

import com.toy.shopwebmvc.config.security.JwtTokenProvider;
import com.toy.shopwebmvc.constant.ApiResponseCode;
import com.toy.shopwebmvc.domain.Token;
import com.toy.shopwebmvc.dto.request.LoginRequest;
import com.toy.shopwebmvc.dto.request.TokenRequest;
import com.toy.shopwebmvc.dto.response.TokenResponse;
import com.toy.shopwebmvc.exception.CommonException;
import com.toy.shopwebmvc.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        String accessToken = jwtTokenProvider.createAccessToken(authentication);
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

        String refreshToken = tokenRepository.findById((String) claims.get("account"))
                .map(Token::getRefreshToken)
                .orElseThrow(() -> new CommonException(ApiResponseCode.BAD_CREDENTIALS));

        if (!tokenRequest.refreshToken().equals(refreshToken)) {
            throw new CommonException(ApiResponseCode.BAD_CREDENTIALS);
        }

        return new TokenResponse(jwtTokenProvider.createAccessToken(SecurityContextHolder.getContext().getAuthentication()));
    }
}
