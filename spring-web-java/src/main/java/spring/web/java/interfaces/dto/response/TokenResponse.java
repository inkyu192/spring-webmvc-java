package spring.web.java.interfaces.dto.response;

public record TokenResponse(
	String accessToken,
	String refreshToken
) {
}
