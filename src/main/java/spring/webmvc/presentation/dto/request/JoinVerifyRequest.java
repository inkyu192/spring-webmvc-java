package spring.webmvc.presentation.dto.request;

import spring.webmvc.application.dto.command.JoinVerifyRequestCommand;
import spring.webmvc.domain.model.vo.Email;

public record JoinVerifyRequest(
	@jakarta.validation.constraints.Email
	String email
) {
	public JoinVerifyRequestCommand toCommand() {
		return new JoinVerifyRequestCommand(Email.create(email));
	}
}
