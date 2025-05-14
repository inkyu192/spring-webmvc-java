package spring.webmvc.domain.repository;

import java.util.List;
import java.util.Optional;

import spring.webmvc.domain.model.entity.Menu;

public interface MenuRepository {
	Optional<Menu> findById(Long id);

	List<Menu> findAllByPermissionNameIn(Iterable<String> permissions);

	Menu save(Menu menu);

	List<Menu> saveAll(Iterable<Menu> menus);
}
