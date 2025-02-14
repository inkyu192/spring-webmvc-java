package spring.web.java.infrastructure.persistence;

import static spring.web.java.domain.model.entity.QItem.*;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import spring.web.java.domain.model.entity.Item;

@Component
public class ItemQuerydslRepository {

	private final JPAQueryFactory queryFactory;

	public ItemQuerydslRepository(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	public Page<Item> findAll(Pageable pageable, String name) {
		long count = Objects.requireNonNullElse(
			queryFactory
				.select(item.count())
				.from(item)
				.where(likeName(name))
				.fetchOne(), 0L
		);

		List<Item> content = queryFactory
			.select(item)
			.from(item)
			.where(likeName(name))
			.limit(pageable.getPageSize())
			.offset(pageable.getOffset())
			.fetch();

		return new PageImpl<>(content, pageable, count);
	}

	private BooleanExpression likeName(String name) {
		if (!StringUtils.hasText(name)) {
			return null;
		}
		return item.name.like("%" + name + "%");
	}
}
