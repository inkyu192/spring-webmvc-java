package spring.webmvc.presentation.dto.response;

import java.util.List;

import spring.webmvc.domain.model.entity.Permission;
import spring.webmvc.domain.model.entity.Role;

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
