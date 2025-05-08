package spring.webmvc.application.dto.result;

public record TokenResult(
	String accessToken,
	String refreshToken
) {
}
