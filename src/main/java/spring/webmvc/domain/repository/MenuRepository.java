package spring.webmvc.domain.repository;

import java.util.List;
import java.util.Optional;

import spring.webmvc.domain.model.entity.Menu;

public interface MenuRepository {
	Optional<Menu> findById(Long id);

	List<Menu> findRootMenus(Iterable<String> permissions);

	List<Menu> findChildMenus(Iterable<String> permissions, Long parentId);

	Menu save(Menu menu);

	List<Menu> saveAll(Iterable<Menu> menus);
}
