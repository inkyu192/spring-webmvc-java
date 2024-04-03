package spring.web.java.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.web.java.dto.request.TokenRequest;
import spring.web.java.dto.response.ApiResponse;
import spring.web.java.dto.response.TokenResponse;
import spring.web.java.service.TokenService;

@RestController
@RequestMapping("token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("reissue")
    public ApiResponse<TokenResponse> reissue(@RequestBody TokenRequest tokenRequest) {
        TokenResponse tokenResponse = tokenService.reissue(tokenRequest);

        return new ApiResponse<>(tokenResponse);
    }
}
