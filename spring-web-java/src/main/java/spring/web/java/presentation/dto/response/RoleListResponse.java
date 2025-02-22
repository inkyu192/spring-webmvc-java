package spring.web.java.presentation.dto.response;

import spring.web.java.domain.model.entity.Role;

public record RoleListResponse(
	Long id,
	String name
) {
	public RoleListResponse(Role role) {
		this(role.getId(), role.getName());
	}
}
