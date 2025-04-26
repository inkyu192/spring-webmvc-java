package spring.webmvc.presentation.controller;

import org.springframework.data.util.Pair;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
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
		Pair<String, String> pair = authService.login(memberLoginRequest.account(), memberLoginRequest.password());
		return new TokenResponse(pair.getFirst(), pair.getSecond());
	}

	@PostMapping("/token/refresh")
	public TokenResponse refreshToken(@RequestBody @Validated TokenRequest tokenRequest) {
		Pair<String, String> pair = authService.refreshToken(tokenRequest.accessToken(), tokenRequest.refreshToken());
		return new TokenResponse(pair.getFirst(), pair.getSecond());
	}
}
