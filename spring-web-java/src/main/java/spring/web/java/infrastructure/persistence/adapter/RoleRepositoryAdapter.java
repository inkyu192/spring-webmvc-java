package spring.web.java.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.web.java.domain.model.entity.Role;
import spring.web.java.domain.repository.RoleRepository;
import spring.web.java.infrastructure.persistence.RoleJpaRepository;

@Component
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepository {

	private final RoleJpaRepository jpaRepository;

	@Override
	public Role save(Role role) {
		return jpaRepository.save(role);
	}

	@Override
	public Optional<Role> findById(Long id) {
		return jpaRepository.findById(id);
	}
}
