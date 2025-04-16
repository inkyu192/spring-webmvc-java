package spring.webmvc.infrastructure.persistence.adapter;

import java.util.List;

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
	public List<Permission> findAllById(Iterable<Long> ids) {
		return jpaRepository.findAllById(ids);
	}
}
