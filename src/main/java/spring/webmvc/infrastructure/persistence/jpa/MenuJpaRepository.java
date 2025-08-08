package spring.webmvc.infrastructure.persistence.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import spring.webmvc.domain.model.entity.Menu;

public interface MenuJpaRepository extends JpaRepository<Menu, Long> {

	@Query("""
			select m
			from Menu m
			left join m.permissionMenus mp
			left join mp.permission p
			where p.name in (:permissions)
			and m.parent is null
		""")
	List<Menu> findRootMenus(Iterable<String> permissions);

	@Query("""
			select m
			from Menu m
			left join m.permissionMenus mp
			left join mp.permission p
			where p.name in (:permissions)
			and m.parent.id = :parentId
		""")
	List<Menu> findChildMenus(Iterable<String> permissions, Long parentId);
}
