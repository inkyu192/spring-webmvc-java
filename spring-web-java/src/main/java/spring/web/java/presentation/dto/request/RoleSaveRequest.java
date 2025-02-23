package spring.web.java.presentation.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record RoleSaveRequest(
	@NotBlank
	String name,
	@NotEmpty
	List<Long> permissionIds
) {
}
