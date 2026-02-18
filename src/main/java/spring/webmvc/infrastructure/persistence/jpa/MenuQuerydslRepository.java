package spring.webmvc.infrastructure.persistence.jpa;

import static spring.webmvc.domain.model.entity.QPermission.*;
import static spring.webmvc.domain.model.entity.QPermissionMenu.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import spring.webmvc.domain.model.entity.Menu;

@Repository
public class MenuQuerydslRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public MenuQuerydslRepository(JPAQueryFactory jpaQueryFactory) {
		this.jpaQueryFactory = jpaQueryFactory;
	}

	public List<Menu> findAllByPermissions(Iterable<String> permissions) {
		List<String> permissionList = new ArrayList<>();
		permissions.forEach(permissionList::add);

		return jpaQueryFactory
			.select(permissionMenu.menu)
			.from(permissionMenu)
			.join(permissionMenu.permission, permission)
			.where(permission.name.in(permissionList))
			.fetch();
	}
}
