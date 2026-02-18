package spring.webmvc.infrastructure.external.notification;

import jakarta.validation.constraints.Email;
import spring.webmvc.application.dto.command.VerifyEmailCommand;

public record VerifyEmailRequest(
	@Email
	String email,
	String verifyLink
) {
	public VerifyEmailCommand toCommand() {
		return new VerifyEmailCommand(email, verifyLink);
	}
}
