package spring.web.java.infrastructure.persistence.adapter;

import java.util.List;

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
	public List<Permission> findAll() {
		return jpaRepository.findAll();
	}

	@Override
	public List<Permission> findAllById(Iterable<Long> ids) {
		return jpaRepository.findAllById(ids);
	}

}
