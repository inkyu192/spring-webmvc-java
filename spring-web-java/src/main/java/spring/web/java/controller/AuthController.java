package spring.web.java.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.web.java.dto.request.LoginRequest;
import spring.web.java.dto.request.TokenRequest;
import spring.web.java.dto.response.ApiResponse;
import spring.web.java.dto.response.TokenResponse;
import spring.web.java.service.AuthService;

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
