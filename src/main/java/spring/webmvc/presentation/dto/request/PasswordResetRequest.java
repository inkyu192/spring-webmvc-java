package spring.webmvc.presentation.dto.request;

import spring.webmvc.application.dto.command.PasswordResetRequestCommand;
import spring.webmvc.domain.model.vo.Email;

public record PasswordResetRequest(
	String email
) {
	public PasswordResetRequestCommand toCommand() {
		return new PasswordResetRequestCommand(Email.create(email));
	}
}
