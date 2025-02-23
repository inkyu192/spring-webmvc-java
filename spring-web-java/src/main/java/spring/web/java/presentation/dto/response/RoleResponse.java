package spring.web.java.presentation.dto.response;

import java.util.List;

import spring.web.java.domain.model.entity.Permission;
import spring.web.java.domain.model.entity.Role;

public record RoleResponse(
	Long id,
	String name,
	List<PermissionResponse> permissions
) {
	public RoleResponse(Role role, List<Permission> permissions) {
		this(
			role.getId(),
			role.getName(),
			permissions.stream()
				.map(PermissionResponse::new)
				.toList()
		);
	}
}
