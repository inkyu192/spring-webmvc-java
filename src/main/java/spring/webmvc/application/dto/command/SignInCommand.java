package spring.webmvc.application.dto.command;

import spring.webmvc.domain.model.vo.Email;

public record SignInCommand(
	Email email,
	String password
) {
}
