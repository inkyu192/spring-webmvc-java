package spring.webmvc.infrastructure.persistence.jpa;

import static spring.webmvc.domain.model.entity.QOrderProduct.*;
import static spring.webmvc.domain.model.entity.QProduct.*;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Product;

@Repository
@RequiredArgsConstructor
public class ProductQuerydslRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public Page<Product> findAll(Pageable pageable, String name) {
		long count = Objects.requireNonNullElse(
			jpaQueryFactory
				.select(product.count())
				.from(product)
				.where(likeName(name))
				.fetchOne(), 0L
		);

		List<Product> content = jpaQueryFactory
			.selectFrom(product)
			.leftJoin(orderProduct).on(product.id.eq(orderProduct.product.id))
			.where(likeName(name))
			.groupBy(product.id)
			.orderBy(orderProduct.count().desc())
			.limit(pageable.getPageSize())
			.offset(pageable.getOffset())
			.fetch();

		return new PageImpl<>(content, pageable, count);
	}

	private BooleanExpression likeName(String name) {
		if (!StringUtils.hasText(name)) {
			return null;
		}
		return product.name.like("%" + name + "%");
	}
}
