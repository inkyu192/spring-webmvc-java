package com.toy.shopwebmvc.controller;

import com.toy.shopwebmvc.dto.request.TokenRequest;
import com.toy.shopwebmvc.dto.response.ApiResponse;
import com.toy.shopwebmvc.dto.response.TokenResponse;
import com.toy.shopwebmvc.service.TokenService;
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

    private final TokenService tokenService;

    @PostMapping("refresh")
    public ApiResponse<TokenResponse> refresh(@RequestBody @Valid TokenRequest tokenRequest) {
        return new ApiResponse<>(tokenService.refresh(tokenRequest));
    }
}
