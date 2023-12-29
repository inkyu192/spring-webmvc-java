package com.webmvc.javaapi.controller;

import com.webmvc.javaapi.config.security.JwtTokenProvider;
import com.webmvc.javaapi.dto.request.TokenRequest;
import com.webmvc.javaapi.dto.response.ApiResponse;
import com.webmvc.javaapi.dto.response.TokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("token")
@RequiredArgsConstructor
public class TokenController {

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("refresh")
    public ApiResponse<TokenResponse> refresh(@RequestBody @Valid TokenRequest tokenRequest) {
//        return new ApiResponse<>(jwtTokenProvider.refresh(tokenRequest));
        return null;
    }
}
