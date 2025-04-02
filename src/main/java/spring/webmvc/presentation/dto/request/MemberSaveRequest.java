package spring.webmvc.presentation.dto.request;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record MemberSaveRequest(
	@NotBlank
	@Email
	String account,
	@NotBlank
	String password,
	@NotBlank
	String name,
	@Pattern(regexp = "^010-\\d{3,4}-\\d{4}$")
	String phone,
	@NotNull
	LocalDate birthDate,
	List<Long> roleIds,
	List<Long> permissionIds
) {
	public MemberSaveRequest {
		if (roleIds == null) {
			roleIds = List.of();
		}

		if (permissionIds == null) {
			permissionIds = List.of();
		}
	}
}
