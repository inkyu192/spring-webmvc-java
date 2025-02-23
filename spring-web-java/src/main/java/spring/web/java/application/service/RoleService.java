package spring.web.java.application.service;

import org.springframework.http.HttpStatus;
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
import spring.web.java.presentation.exception.BaseException;
import spring.web.java.presentation.exception.ErrorCode;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoleService {

	private final RoleRepository roleRepository;
	private final PermissionRepository permissionRepository;

	@Transactional
	public RoleResponse saveRole(RoleSaveRequest roleSaveRequest) {
		Role role = Role.create(roleSaveRequest.name());

		roleSaveRequest.permissionIds().forEach(id -> {
			Permission permission = permissionRepository.findById(id)
				.orElseThrow(() -> new BaseException(ErrorCode.DATA_NOT_FOUND, HttpStatus.NOT_FOUND));

			role.addPermission(RolePermission.create(permission));
		});

		roleRepository.save(role);

		return new RoleResponse(
			role,
			role.getRolePermissions().stream()
				.map(RolePermission::getPermission)
				.toList()
		);
	}
}
