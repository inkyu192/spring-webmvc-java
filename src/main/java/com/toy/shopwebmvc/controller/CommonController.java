package com.toy.shopwebmvc.controller;

import com.toy.shopwebmvc.dto.request.LoginRequest;
import com.toy.shopwebmvc.dto.request.TokenRequest;
import com.toy.shopwebmvc.dto.response.ApiResponse;
import com.toy.shopwebmvc.dto.response.TokenResponse;
import com.toy.shopwebmvc.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommonController {

    private final CommonService commonService;

    @PostMapping("login")
    public ApiResponse<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        TokenResponse tokenResponse = commonService.login(loginRequest);

        return new ApiResponse<>(tokenResponse);
    }

    @PostMapping("refresh")
    public ApiResponse<TokenResponse> refresh(@RequestBody TokenRequest tokenRequest) {
        TokenResponse tokenResponse = commonService.refresh(tokenRequest);

        return new ApiResponse<>(tokenResponse);
    }
}
