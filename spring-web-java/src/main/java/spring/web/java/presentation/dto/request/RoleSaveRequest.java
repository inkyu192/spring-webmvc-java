package spring.web.java.presentation.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RoleSaveRequest(
	@NotBlank
	String name,
	@Size(min = 1)
	List<Long> permissionIds
) {
	public RoleSaveRequest {
		if (permissionIds == null) {
			permissionIds = List.of();
		}
	}
}
