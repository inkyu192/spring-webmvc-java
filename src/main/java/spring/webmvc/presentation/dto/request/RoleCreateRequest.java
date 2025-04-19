package spring.webmvc.presentation.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RoleCreateRequest(
	@NotBlank
	String name,
	@Size(min = 1)
	List<Long> permissionIds
) {
	public RoleCreateRequest {
		if (permissionIds == null) {
			permissionIds = List.of();
		}
	}
}
