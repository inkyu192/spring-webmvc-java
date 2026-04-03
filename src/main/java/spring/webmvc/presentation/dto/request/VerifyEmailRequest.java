package spring.webmvc.presentation.dto.request;

import spring.webmvc.application.dto.command.VerifyEmailCommand;

public record VerifyEmailRequest(
	@jakarta.validation.constraints.Email
	String email,
	String verifyLink
) {
	public VerifyEmailCommand toCommand() {
		return new VerifyEmailCommand(email, verifyLink);
	}
}
