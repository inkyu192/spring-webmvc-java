package spring.webmvc.domain.repository;

import java.util.List;

import spring.webmvc.domain.model.entity.Menu;

public interface MenuRepository {
	List<Menu> findAllWithRecursiveByPermissions(Iterable<String> permissions);
}
