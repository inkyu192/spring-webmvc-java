package spring.webmvc.domain.repository;

import java.util.List;

import spring.webmvc.domain.model.entity.Role;

public interface RoleRepository {
	Role save(Role role);

	List<Role> saveAll(Iterable<Role> roles);

	List<Role> findAllById(Iterable<Long> ids);
}
