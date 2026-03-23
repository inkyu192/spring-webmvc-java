package spring.webmvc.infrastructure.persistence.adapter;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Menu;
import spring.webmvc.domain.repository.MenuRepository;
import spring.webmvc.infrastructure.persistence.jpa.MenuJpaRepository;

@Component
@RequiredArgsConstructor
public class MenuRepositoryAdapter implements MenuRepository {

	private final MenuJpaRepository jpaRepository;

	@Override
	public List<Menu> findAllWithRecursiveByPermissions(Iterable<String> permissions) {
		return jpaRepository.findAllWithRecursiveByPermissions(permissions);
	}
}
