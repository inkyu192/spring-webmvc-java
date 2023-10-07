package com.toy.shopwebmvc.controller;

import com.toy.shopwebmvc.dto.response.ApiResponse;
import com.toy.shopwebmvc.dto.response.TokenResponse;
import com.toy.shopwebmvc.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("refresh")
    public ApiResponse<TokenResponse> refresh(HttpServletRequest request, @RequestParam String refreshToken) {
        String accessToken = tokenService.getAccessToken(request);

        return new ApiResponse<>(tokenService.refresh(accessToken, refreshToken));
    }
}
