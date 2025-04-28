package spring.webmvc.application.service;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Permission;
import spring.webmvc.domain.repository.PermissionRepository;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PermissionService {

	private final PermissionRepository permissionRepository;

	public void addPermission(List<Long> permissionIds, Consumer<Permission> consumer) {
		Map<Long, Permission> permissionMap = permissionRepository.findAllById(permissionIds).stream()
			.collect(Collectors.toMap(Permission::getId, permission -> permission));

		for (Long id : permissionIds) {
			Permission permission = permissionMap.get(id);
			if (permission == null) {
				throw new EntityNotFoundException(Permission.class, id);
			}
			consumer.accept(permission);
		}
	}
}
