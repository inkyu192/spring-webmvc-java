package spring.webmvc.presentation.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.dto.result.TokenResult;
import spring.webmvc.application.service.AuthService;
import spring.webmvc.presentation.dto.request.MemberLoginRequest;
import spring.webmvc.presentation.dto.request.TokenRequest;
import spring.webmvc.presentation.dto.response.TokenResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public TokenResponse login(@RequestBody @Validated MemberLoginRequest memberLoginRequest) {
		TokenResult tokenResult = authService.login(memberLoginRequest.email(), memberLoginRequest.password());
		return new TokenResponse(tokenResult);
	}

	@PostMapping("/token/refresh")
	public TokenResponse refreshToken(@RequestBody @Validated TokenRequest tokenRequest) {
		TokenResult tokenResult = authService.refreshToken(tokenRequest.accessToken(), tokenRequest.refreshToken());
		return new TokenResponse(tokenResult);
	}
}
