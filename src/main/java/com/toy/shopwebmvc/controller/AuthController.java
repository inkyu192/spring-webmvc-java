package com.toy.shopwebmvc.controller;

import com.toy.shopwebmvc.dto.request.LoginRequest;
import com.toy.shopwebmvc.dto.request.TokenRequest;
import com.toy.shopwebmvc.dto.response.ApiResponse;
import com.toy.shopwebmvc.dto.response.TokenResponse;
import com.toy.shopwebmvc.service.AuthService;
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
