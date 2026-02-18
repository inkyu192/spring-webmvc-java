package spring.webmvc.application.dto.command;

public record RefreshTokenCommand(
	String accessToken,
	String refreshToken
) {
}
