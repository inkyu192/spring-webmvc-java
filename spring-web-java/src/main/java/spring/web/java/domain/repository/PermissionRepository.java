package spring.web.java.domain.repository;

import java.util.Optional;

import spring.web.java.domain.model.entity.Permission;

public interface PermissionRepository {
	Optional<Permission> findById(Long id);
}
