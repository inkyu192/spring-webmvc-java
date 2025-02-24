package spring.web.java.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TokenRequest(
	@NotBlank
	String accessToken,
	@NotBlank
	String refreshToken
) {
}
