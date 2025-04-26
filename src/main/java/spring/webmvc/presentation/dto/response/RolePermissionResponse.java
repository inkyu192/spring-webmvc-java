package spring.webmvc.presentation.dto.response;

import spring.webmvc.domain.model.entity.RolePermission;

public record RolePermissionResponse(
	Long id,
	String name
) {
	public RolePermissionResponse(RolePermission rolePermission) {
		this(rolePermission.getPermission().getId(), rolePermission.getPermission().getName());
	}
}
