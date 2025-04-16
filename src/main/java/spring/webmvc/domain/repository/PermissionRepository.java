package spring.webmvc.domain.repository;

import java.util.List;

import spring.webmvc.domain.model.entity.Permission;

public interface PermissionRepository {
	List<Permission> findAllById(Iterable<Long> ids);
}
