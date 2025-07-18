package spring.webmvc.infrastructure.persistence.adapter;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Role;
import spring.webmvc.domain.repository.RoleRepository;
import spring.webmvc.infrastructure.persistence.jpa.RoleJpaRepository;

@Component
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepository {

	private final RoleJpaRepository jpaRepository;

	@Override
	public Role save(Role role) {
		return jpaRepository.save(role);
	}

	@Override
	public List<Role> saveAll(Iterable<Role> roles) {
		return jpaRepository.saveAll(roles);
	}

	@Override
	public List<Role> findAllById(Iterable<Long> ids) {
		return jpaRepository.findAllById(ids);
	}
}
