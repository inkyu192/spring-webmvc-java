package spring.web.java.domain.token.dto;

public record TokenResponse(
	String accessToken,
	String refreshToken
) {
}
