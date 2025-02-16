package spring.web.java.presentation.dto.response;

public record TokenResponse(
	String accessToken,
	String refreshToken
) {
}
