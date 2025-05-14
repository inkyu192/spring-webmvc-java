package spring.webmvc.infrastructure.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import spring.webmvc.domain.model.entity.Menu;

public interface MenuJpaRepository extends JpaRepository<Menu, Long> {

	@Query("""
		select m
		from Menu m
		left join m.menuPermissions mp
		left join mp.permission p
		where p.name in (:permissions)
	""")
	List<Menu> findAllByPermissionNameIn(Iterable<String> permissions);
}
