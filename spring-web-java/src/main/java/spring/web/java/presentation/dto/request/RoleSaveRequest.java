package spring.web.java.presentation.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public record RoleSaveRequest(
	@NotBlank
	String name,
	List<Long> permissionIds
) {
}
