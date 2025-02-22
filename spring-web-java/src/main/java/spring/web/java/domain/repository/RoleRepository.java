package spring.web.java.domain.repository;

import java.util.List;
import java.util.Optional;

import spring.web.java.domain.model.entity.Role;

public interface RoleRepository {
	Role save(Role role);
	List<Role> findAll();
	Optional<Role> findById(Long id);
	void delete(Role role);
}
