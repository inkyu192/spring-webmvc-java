package spring.webmvc.presentation.dto.request;

import spring.webmvc.application.dto.command.PasswordResetEmailCommand;

public record PasswordResetEmailRequest(
	@jakarta.validation.constraints.Email
	String email,
	String resetUrl
) {
	public PasswordResetEmailCommand toCommand() {
		return new PasswordResetEmailCommand(email, resetUrl);
	}
}
