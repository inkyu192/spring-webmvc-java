package spring.web.java.application.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.web.java.domain.model.entity.Permission;
import spring.web.java.domain.model.entity.Role;
import spring.web.java.domain.model.entity.RolePermission;
import spring.web.java.domain.repository.RoleRepository;
import spring.web.java.domain.repository.PermissionRepository;
import spring.web.java.presentation.dto.request.RoleSaveRequest;
import spring.web.java.presentation.dto.response.RoleDetailResponse;
import spring.web.java.presentation.dto.response.RoleListResponse;
import spring.web.java.presentation.exception.BaseException;
import spring.web.java.presentation.exception.ErrorCode;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoleService {

	private final RoleRepository roleRepository;
	private final PermissionRepository permissionRepository;

	@Transactional
	public RoleListResponse saveRole(RoleSaveRequest request) {
		List<Permission> permissions = permissionRepository.findAllById(request.permissionIds());
		List<RolePermission> rolePermissions = permissions.stream()
			.map(RolePermission::create)
			.toList();

		Role role = roleRepository.save(Role.create(request.name(), rolePermissions));

		return new RoleListResponse(role);
	}

	public List<RoleListResponse> findRoles() {
		return roleRepository.findAll().stream()
			.map(RoleListResponse::new)
			.toList();
	}

	public RoleDetailResponse findRole(Long id) {
		Role role = roleRepository.findById(id)
			.orElseThrow(() -> new BaseException(ErrorCode.DATA_NOT_FOUND, HttpStatus.NOT_FOUND));

		List<Permission> permissions = role.getPermissions().stream()
			.map(RolePermission::getPermission)
			.toList();

		return new RoleDetailResponse(role, permissions);
	}

	public void deleteRole(Long id) {
		roleRepository.findById(id).ifPresent(roleRepository::delete);
	}
}
