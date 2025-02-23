package spring.web.java.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import spring.web.java.domain.model.entity.Permission;
import spring.web.java.domain.repository.PermissionRepository;
import spring.web.java.infrastructure.persistence.PermissionJpaRepository;

@Repository
@RequiredArgsConstructor
public class PermissionRepositoryAdapter implements PermissionRepository {

	private final PermissionJpaRepository jpaRepository;

	@Override
	public Optional<Permission> findById(Long id) {
		return jpaRepository.findById(id);
	}
}
