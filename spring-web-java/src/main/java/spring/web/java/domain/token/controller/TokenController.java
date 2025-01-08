package spring.web.java.domain.token.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import spring.web.java.domain.token.serivce.TokenService;
import spring.web.java.domain.token.dto.TokenRequest;
import spring.web.java.domain.token.dto.TokenResponse;

@RestController
@RequestMapping("token")
@RequiredArgsConstructor
public class TokenController {

	private final TokenService tokenService;

	@PostMapping("reissue")
	public TokenResponse reissue(@RequestBody TokenRequest tokenRequest) {
		return tokenService.reissue(tokenRequest);
	}
}
