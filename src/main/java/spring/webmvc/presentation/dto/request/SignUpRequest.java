package spring.webmvc.presentation.dto.request;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Pattern;
import spring.webmvc.application.dto.command.SignUpCommand;
import spring.webmvc.domain.model.enums.Gender;
import spring.webmvc.domain.model.vo.Email;
import spring.webmvc.domain.model.vo.Phone;

public record SignUpRequest(
	@jakarta.validation.constraints.Email
	String email,
	String password,
	String name,
	Gender gender,
	@Pattern(regexp = "^010-\\d{3,4}-\\d{4}$")
	String phone,
	LocalDate birthday,
	String profileImageKey,
	List<Long> roleIds,
	List<Long> permissionIds
) {
	public SignUpCommand toCommand() {
		return new SignUpCommand(
			Email.create(email),
			password,
			name,
			Phone.create(phone),
			gender,
			birthday,
			profileImageKey,
			roleIds,
			permissionIds
		);
	}
}
