package spring.web.java.presentation.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public record MemberSaveRequest(
	@NotBlank
	String account,
	@NotBlank
	String password,
	@NotBlank
	String name,
	String city,
	String street,
	String zipcode,
	List<Long> roleIds,
	List<Long> permissionIds
) {
}
