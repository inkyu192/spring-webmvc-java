package spring.webmvc.presentation.dto.response;

import spring.webmvc.domain.model.entity.Permission;

public record PermissionResponse(
	Long id,
	String name
) {
	public PermissionResponse(Permission permission) {
		this(permission.getId(), permission.getName());
	}
}
