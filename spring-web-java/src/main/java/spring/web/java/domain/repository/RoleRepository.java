package spring.web.java.domain.repository;

import java.util.Optional;

import spring.web.java.domain.model.entity.Role;

public interface RoleRepository {
	Role save(Role role);
	Optional<Role> findById(Long id);
}
