package spring.web.java.presentation.dto.response;

import java.util.List;

import spring.web.java.domain.model.entity.Permission;
import spring.web.java.domain.model.entity.Role;

public record RoleDetailResponse(
	Long id,
	String name,
	List<PermissionResponse> roles
) {
	public RoleDetailResponse(Role role, List<Permission> permissions) {
		this(
			role.getId(),
			role.getName(),
			permissions.stream()
				.map(PermissionResponse::new)
				.toList()
		);
	}
}
