package spring.webmvc.application.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Permission;
import spring.webmvc.domain.model.entity.Role;
import spring.webmvc.domain.model.entity.RolePermission;
import spring.webmvc.domain.repository.PermissionRepository;
import spring.webmvc.domain.repository.RoleRepository;
import spring.webmvc.presentation.dto.request.RoleSaveRequest;
import spring.webmvc.presentation.dto.response.RoleResponse;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoleService {

	private final RoleRepository roleRepository;
	private final PermissionRepository permissionRepository;

	@Transactional
	public RoleResponse saveRole(RoleSaveRequest roleSaveRequest) {
		List<RolePermission> rolePermissions = new ArrayList<>();
		for (Long id : roleSaveRequest.permissionIds()) {
			Permission permission = permissionRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(Permission.class, id));

			rolePermissions.add(RolePermission.create(permission));
		}

		Role role = roleRepository.save(
			Role.create(roleSaveRequest.name(), rolePermissions)
		);

		return new RoleResponse(
			role,
			role.getRolePermissions().stream()
				.map(RolePermission::getPermission)
				.toList()
		);
	}
}
