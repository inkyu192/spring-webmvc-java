package spring.webmvc.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

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
	public Optional<Menu> findById(Long id) {
		return jpaRepository.findById(id);
	}

	@Override
	public List<Menu> findRootMenus(Iterable<String> permissions) {
		return jpaRepository.findRootMenus(permissions);
	}

	@Override
	public List<Menu> findChildMenus(Iterable<String> permissions, Long parentId) {
		return jpaRepository.findChildMenus(permissions, parentId);
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
