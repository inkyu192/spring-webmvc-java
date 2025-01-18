package spring.web.java.domain.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import spring.web.java.domain.auth.service.AuthService;
import spring.web.java.domain.member.dto.MemberLoginRequest;
import spring.web.java.domain.token.dto.TokenRequest;
import spring.web.java.domain.token.dto.TokenResponse;

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
