package spring.webmvc.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Role;
import spring.webmvc.domain.repository.RoleRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoleService {

	private final RoleRepository roleRepository;
	private final PermissionService permissionService;

	@Transactional
	public Role createRole(String name, List<Long> permissionIds) {
		Role role = Role.create(name);

		permissionService.addPermission(permissionIds, role::addPermission);

		return roleRepository.save(role);
	}
}
