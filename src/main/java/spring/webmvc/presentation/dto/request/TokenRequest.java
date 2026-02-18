package spring.webmvc.presentation.dto.request;

import spring.webmvc.application.dto.command.RefreshTokenCommand;

public record TokenRequest(
	String accessToken,
	String refreshToken
) {
	public RefreshTokenCommand toCommand() {
		return new RefreshTokenCommand(accessToken, refreshToken);
	}
}
