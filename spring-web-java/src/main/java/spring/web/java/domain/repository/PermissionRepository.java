package spring.web.java.domain.repository;

import java.util.List;

import spring.web.java.domain.model.entity.Permission;

public interface PermissionRepository {
	List<Permission> findAll();
	List<Permission> findAllById(Iterable<Long> ids);
}
