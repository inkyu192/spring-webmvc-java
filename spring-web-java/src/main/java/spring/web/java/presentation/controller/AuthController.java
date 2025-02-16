package spring.web.java.presentation.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import spring.web.java.application.service.AuthService;
import spring.web.java.presentation.dto.request.MemberLoginRequest;
import spring.web.java.presentation.dto.request.TokenRequest;
import spring.web.java.presentation.dto.response.TokenResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public TokenResponse login(@RequestBody MemberLoginRequest memberLoginRequest) {
		return authService.login(memberLoginRequest);
	}

	@PostMapping("/token/refresh")
	public TokenResponse refreshToken(@RequestBody TokenRequest tokenRequest) {
		return authService.refreshToken(tokenRequest);
	}
}
