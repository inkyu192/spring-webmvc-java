package spring.web.java.presentation.dto.response;

import spring.web.java.domain.model.entity.Permission;

public record PermissionResponse(
	Long id,
	String name
) {
	public PermissionResponse(Permission permission) {
		this(permission.getId(), permission.getName());
	}
}
