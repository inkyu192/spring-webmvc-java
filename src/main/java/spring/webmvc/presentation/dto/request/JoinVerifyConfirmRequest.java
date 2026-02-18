package spring.webmvc.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import spring.webmvc.application.dto.command.JoinVerifyConfirmCommand;

public record JoinVerifyConfirmRequest(
	@NotBlank String token
) {
	public JoinVerifyConfirmCommand toCommand() {
		return new JoinVerifyConfirmCommand(token);
	}
}
