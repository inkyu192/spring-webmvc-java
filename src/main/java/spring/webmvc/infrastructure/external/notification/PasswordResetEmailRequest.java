package spring.webmvc.infrastructure.external.notification;

import jakarta.validation.constraints.Email;
import spring.webmvc.application.dto.command.VerifyEmailCommand;

public record PasswordResetEmailRequest(
	@Email
	String email,
	String resetUrl
) {
	public VerifyEmailCommand toCommand() {
		return new VerifyEmailCommand(email, resetUrl);
	}
}
