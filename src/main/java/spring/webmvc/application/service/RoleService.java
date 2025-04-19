package spring.webmvc.application.service;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Permission;
import spring.webmvc.domain.model.entity.Role;
import spring.webmvc.domain.model.entity.RolePermission;
import spring.webmvc.domain.repository.PermissionRepository;
import spring.webmvc.domain.repository.RoleRepository;
import spring.webmvc.presentation.dto.request.RoleCreateRequest;
import spring.webmvc.presentation.dto.response.RoleResponse;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoleService {

	private final RoleRepository roleRepository;
	private final PermissionRepository permissionRepository;

	@Transactional
	public RoleResponse createRole(RoleCreateRequest roleCreateRequest) {
		Role role = Role.create(roleCreateRequest.name());

		Map<Long, Permission> permissionMap = permissionRepository.findAllById(roleCreateRequest.permissionIds()).stream()
			.collect(Collectors.toMap(Permission::getId, permission -> permission));

		for (Long id : roleCreateRequest.permissionIds()) {
			Permission permission = permissionMap.get(id);
			if (permission == null) {
				throw new EntityNotFoundException(Permission.class, id);
			}
			role.addPermission(permission);
		}

		roleRepository.save(role);

		return new RoleResponse(
			role,
			role.getRolePermissions().stream()
				.map(RolePermission::getPermission)
				.toList()
		);
	}
}
