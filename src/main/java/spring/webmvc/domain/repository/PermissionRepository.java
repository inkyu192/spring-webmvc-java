package spring.webmvc.domain.repository;

import java.util.Optional;

import spring.webmvc.domain.model.entity.Permission;

public interface PermissionRepository {
	Optional<Permission> findById(Long id);
}
