package spring.webmvc.infrastructure.persistence.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import spring.webmvc.domain.model.entity.Menu;

public interface MenuJpaRepository extends JpaRepository<Menu, Long> {

	@Query(value = """
		WITH RECURSIVE menu_hierarchy AS (
			SELECT child.*
			FROM menu child
			INNER JOIN permission_menu pm ON pm.menu_id = child.id
			INNER JOIN permission p ON p.id = pm.permission_id
			WHERE p.name IN (:permissions)
			UNION
			SELECT parent.*
			FROM menu parent
			INNER JOIN menu_hierarchy child ON child.parent_id = parent.id
		)
		SELECT mh.*
		FROM menu_hierarchy mh
		""", nativeQuery = true)
	List<Menu> findAllWithRecursiveByPermissions(Iterable<String> permissions);
}
