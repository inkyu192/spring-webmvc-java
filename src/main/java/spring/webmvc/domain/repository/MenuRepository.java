package spring.webmvc.domain.repository;

import java.util.List;

import spring.webmvc.domain.model.entity.Menu;

public interface MenuRepository {
	List<Menu> findAllById(List<Long> ids);

	List<Menu> findAllByPermissions(Iterable<String> permissions);

	List<Menu> saveAll(Iterable<Menu> menus);
}
