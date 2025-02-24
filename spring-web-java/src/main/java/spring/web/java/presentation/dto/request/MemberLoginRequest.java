package spring.web.java.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberLoginRequest(
	@NotBlank
	String account,
	@NotBlank
	String password
) {
}
