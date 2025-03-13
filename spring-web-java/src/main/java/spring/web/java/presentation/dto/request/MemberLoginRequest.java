package spring.web.java.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberLoginRequest(
	@NotBlank
	@Email
	String account,
	@NotBlank
	String password
) {
}
