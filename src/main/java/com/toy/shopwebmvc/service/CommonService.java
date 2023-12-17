package com.toy.shopwebmvc.service;

import com.toy.shopwebmvc.config.security.JwtTokenProvider;
import com.toy.shopwebmvc.dto.request.LoginRequest;
import com.toy.shopwebmvc.dto.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationConfiguration authenticationConfiguration;

    @SneakyThrows
    public TokenResponse login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.account(), loginRequest.password());

        Authentication authentication = authenticationConfiguration.getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);


        return new TokenResponse(jwtTokenProvider.createAccessToken(authentication));
    }
}
