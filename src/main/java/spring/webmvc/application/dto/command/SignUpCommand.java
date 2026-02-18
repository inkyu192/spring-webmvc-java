package spring.webmvc.application.dto.command;

import java.time.LocalDate;
import java.util.List;

import spring.webmvc.domain.model.enums.Gender;
import spring.webmvc.domain.model.vo.Email;
import spring.webmvc.domain.model.vo.Phone;

public record SignUpCommand(
	Email email,
	String password,
	String name,
	Phone phone,
	Gender gender,
	LocalDate birthday,
	String profileImageKey,
	List<Long> roleIds,
	List<Long> permissionIds
) {
}
