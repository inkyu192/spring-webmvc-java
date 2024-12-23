package spring.web.java.dto.response;

public record TokenResponse(
	String accessToken,
	String refreshToken
) {
}
