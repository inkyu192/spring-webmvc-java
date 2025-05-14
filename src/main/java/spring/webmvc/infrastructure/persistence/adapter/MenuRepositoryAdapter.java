package spring.webmvc.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Menu;
import spring.webmvc.domain.repository.MenuRepository;
import spring.webmvc.infrastructure.persistence.MenuJpaRepository;

@Component
@RequiredArgsConstructor
public class MenuRepositoryAdapter implements MenuRepository {

	private final MenuJpaRepository jpaRepository;

	@Override
	public Optional<Menu> findById(Long id) {
		return jpaRepository.findById(id);
	}

	@Override
	public List<Menu> findAllByPermissionNameIn(Iterable<String> permissions) {
		return jpaRepository.findAllByPermissionNameIn(permissions);
	}

	@Override
	public Menu save(Menu menu) {
		return jpaRepository.save(menu);
	}

	@Override
	public List<Menu> saveAll(Iterable<Menu> menus) {
		return jpaRepository.saveAll(menus);
	}
}
