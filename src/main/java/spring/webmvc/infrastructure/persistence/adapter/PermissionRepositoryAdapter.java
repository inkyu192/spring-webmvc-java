package spring.webmvc.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Permission;
import spring.webmvc.domain.repository.PermissionRepository;
import spring.webmvc.infrastructure.persistence.PermissionJpaRepository;

@Component
@RequiredArgsConstructor
public class PermissionRepositoryAdapter implements PermissionRepository {

	private final PermissionJpaRepository jpaRepository;

	@Override
	public Optional<Permission> findById(Long id) {
		return jpaRepository.findById(id);
	}
}
