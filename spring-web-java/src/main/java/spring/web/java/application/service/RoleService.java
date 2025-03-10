package spring.web.java.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.web.java.domain.model.entity.Permission;
import spring.web.java.domain.model.entity.Role;
import spring.web.java.domain.model.entity.RolePermission;
import spring.web.java.domain.repository.PermissionRepository;
import spring.web.java.domain.repository.RoleRepository;
import spring.web.java.presentation.dto.request.RoleSaveRequest;
import spring.web.java.presentation.dto.response.RoleResponse;
import spring.web.java.presentation.exception.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoleService {

	private final RoleRepository roleRepository;
	private final PermissionRepository permissionRepository;

	@Transactional
	public RoleResponse saveRole(RoleSaveRequest roleSaveRequest) {
		List<RolePermission> rolePermissions = roleSaveRequest.permissionIds().stream()
			.map(id -> {
				Permission permission = permissionRepository.findById(id)
					.orElseThrow(() -> new EntityNotFoundException(Permission.class, id));

				return RolePermission.create(permission);
			})
			.toList();

		Role role = roleRepository.save(Role.create(roleSaveRequest.name(), rolePermissions));

		return new RoleResponse(
			role,
			role.getRolePermissions().stream()
				.map(RolePermission::getPermission)
				.toList()
		);
	}
}
