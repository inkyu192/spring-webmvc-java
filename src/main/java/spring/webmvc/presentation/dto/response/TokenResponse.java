package spring.webmvc.presentation.dto.response;

import spring.webmvc.application.dto.result.TokenResult;

public record TokenResponse(
	String accessToken,
	String refreshToken
) {
	public static TokenResponse of(TokenResult result) {
		return new TokenResponse(result.accessToken(), result.refreshToken());
	}
}
