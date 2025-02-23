package spring.web.java.presentation.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public record MemberSaveRequest(
	@NotNull
	String account,
	@NotNull
	String password,
	@NotNull
	String name,
	@NotNull
	String city,
	@NotNull
	String street,
	@NotNull
	String zipcode,
	List<Long> roleIds,
	List<Long> permissionIds
) {
}
