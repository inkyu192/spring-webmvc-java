package spring.webmvc.infrastructure.persistence;

import static spring.webmvc.domain.model.entity.QItem.*;
import static spring.webmvc.domain.model.entity.QOrderItem.*;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Item;

@Repository
@RequiredArgsConstructor
public class ItemQuerydslRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public Page<Item> findAll(Pageable pageable, String name) {
		long count = Objects.requireNonNullElse(
			jpaQueryFactory
				.select(item.count())
				.from(item)
				.where(likeName(name))
				.fetchOne(), 0L
		);

		List<Tuple> tuples = jpaQueryFactory
			.select(item, orderItem.count())
			.from(item)
			.leftJoin(orderItem).on(item.id.eq(orderItem.item.id))
			.where(likeName(name))
			.groupBy(item.id)
			.orderBy(orderItem.count().desc())
			.limit(pageable.getPageSize())
			.offset(pageable.getOffset())
			.fetch();

		List<Item> content = tuples.stream()
			.map(tuple -> tuple.get(0, Item.class))
			.toList();

		return new PageImpl<>(content, pageable, count);
	}

	private BooleanExpression likeName(String name) {
		if (!StringUtils.hasText(name)) {
			return null;
		}
		return item.name.like("%" + name + "%");
	}
}
