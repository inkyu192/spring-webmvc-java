package spring.webmvc.infrastructure.persistence.jpa;

import static spring.webmvc.domain.model.entity.QProduct.*;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.infrastructure.persistence.dto.CursorPage;

@Repository
@RequiredArgsConstructor
public class ProductQuerydslRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public CursorPage<Product> findAll(Long nextCursorId, int size, String name) {
		List<Product> content = jpaQueryFactory
			.selectFrom(product)
			.where(loeProductId(nextCursorId), likeName(name))
			.orderBy(product.id.desc())
			.limit(size + 1)
			.fetch();

		return new CursorPage<>(content, size, Product::getId);
	}

	private BooleanExpression loeProductId(Long nextCursorId) {
		if (nextCursorId == null) {
			return null;
		}
		return product.id.loe(nextCursorId);
	}

	private BooleanExpression likeName(String name) {
		if (!StringUtils.hasText(name)) {
			return null;
		}
		return product.name.like("%" + name + "%");
	}
}
