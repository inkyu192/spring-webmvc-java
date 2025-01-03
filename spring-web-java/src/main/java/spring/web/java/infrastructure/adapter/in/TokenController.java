package spring.web.java.infrastructure.adapter.in;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import spring.web.java.application.port.in.TokenServicePort;
import spring.web.java.dto.request.TokenRequest;
import spring.web.java.dto.response.TokenResponse;

@RestController
@RequestMapping("token")
@RequiredArgsConstructor
public class TokenController {

	private final TokenServicePort tokenService;

	@PostMapping("reissue")
	public TokenResponse reissue(@RequestBody TokenRequest tokenRequest) {
		return tokenService.reissue(tokenRequest);
	}
}
