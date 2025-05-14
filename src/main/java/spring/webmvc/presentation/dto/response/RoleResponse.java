package spring.webmvc.presentation.dto.response;

import java.util.List;

import spring.webmvc.domain.model.entity.Role;

public record RoleResponse(
	Long id,
	String name,
	List<PermissionResponse> permissions
) {
	public RoleResponse(Role role) {
		this(
			role.getId(),
			role.getName(),
			role.getRolePermissions().stream()
				.map(rolePermission -> new PermissionResponse(rolePermission.getPermission()))
				.toList()
		);
	}
}
