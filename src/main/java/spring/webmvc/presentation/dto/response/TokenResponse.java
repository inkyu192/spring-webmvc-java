package spring.webmvc.presentation.dto.response;

public record TokenResponse(
	String accessToken,
	String refreshToken
) {
}
