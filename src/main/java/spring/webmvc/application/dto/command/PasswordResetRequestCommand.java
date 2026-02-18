package spring.webmvc.application.dto.command;

import spring.webmvc.domain.model.vo.Email;

public record PasswordResetRequestCommand(
	Email email
) {
}
