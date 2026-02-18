package spring.webmvc.presentation.dto.request;

import spring.webmvc.application.dto.command.PasswordResetConfirmCommand;

public record PasswordResetConfirmRequest(
	String token,
	String newPassword
) {
	public PasswordResetConfirmCommand toCommand() {
		return new PasswordResetConfirmCommand(token, newPassword);
	}
}
