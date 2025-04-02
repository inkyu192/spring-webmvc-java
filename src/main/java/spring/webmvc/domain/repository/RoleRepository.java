package spring.webmvc.domain.repository;

import java.util.Optional;

import spring.webmvc.domain.model.entity.Role;

public interface RoleRepository {
	Role save(Role role);
	Optional<Role> findById(Long id);
}
