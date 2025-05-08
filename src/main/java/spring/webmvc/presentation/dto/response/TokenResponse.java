package spring.webmvc.presentation.dto.response;

import spring.webmvc.application.dto.result.TokenResult;

public record TokenResponse(
	String accessToken,
	String refreshToken
) {
	public TokenResponse(TokenResult tokenResult) {
		this(tokenResult.accessToken(), tokenResult.refreshToken());
	}
}
