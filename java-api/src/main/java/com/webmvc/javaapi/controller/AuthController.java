package com.webmvc.javaapi.controller;

import com.webmvc.javaapi.dto.request.LoginRequest;
import com.webmvc.javaapi.dto.request.TokenRequest;
import com.webmvc.javaapi.dto.response.ApiResponse;
import com.webmvc.javaapi.dto.response.TokenResponse;
import com.webmvc.javaapi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    public ApiResponse<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        TokenResponse tokenResponse = authService.login(loginRequest);

        return new ApiResponse<>(tokenResponse);
    }

    @PostMapping("refresh")
    public ApiResponse<TokenResponse> refresh(@RequestBody TokenRequest tokenRequest) {
        TokenResponse tokenResponse = authService.refresh(tokenRequest);

        return new ApiResponse<>(tokenResponse);
    }
}
