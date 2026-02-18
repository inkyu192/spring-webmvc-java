package spring.webmvc.infrastructure.persistence.jpa;

import static spring.webmvc.domain.model.entity.QProduct.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import spring.webmvc.application.dto.query.ProductCursorPageQuery;
import spring.webmvc.application.dto.query.ProductOffsetPageQuery;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.enums.ProductStatus;
import spring.webmvc.infrastructure.persistence.dto.CursorPage;

@Repository
public class ProductQuerydslRepository {

	private static final long DEFAULT_PAGE_SIZE = 10L;

	private final JPAQueryFactory jpaQueryFactory;

	public ProductQuerydslRepository(JPAQueryFactory jpaQueryFactory) {
		this.jpaQueryFactory = jpaQueryFactory;
	}

	public CursorPage<Product> findAllWithCursorPage(ProductCursorPageQuery query) {
		List<Product> content = jpaQueryFactory
			.selectFrom(product)
			.where(
				loeProductId(query.cursorId()),
				likeName(query.name()),
				eqStatus(query.status())
			)
			.orderBy(product.id.desc())
			.limit(DEFAULT_PAGE_SIZE + 1)
			.fetch();

		return CursorPage.create(content, DEFAULT_PAGE_SIZE, Product::getId);
	}

	public Page<Product> findAllWithOffsetPage(ProductOffsetPageQuery query) {
		Long count = jpaQueryFactory
			.select(product.count())
			.from(product)
			.where(
				likeName(query.name()),
				eqStatus(query.status())
			)
			.fetchOne();

		if (count == null) {
			count = 0L;
		}

		List<Product> content = jpaQueryFactory
			.selectFrom(product)
			.where(
				likeName(query.name()),
				eqStatus(query.status())
			)
			.orderBy(product.id.desc())
			.limit(query.pageable().getPageSize())
			.offset(query.pageable().getOffset())
			.fetch();

		return new PageImpl<>(content, query.pageable(), count);
	}

	private BooleanExpression loeProductId(Long cursorId) {
		return cursorId != null ? product.id.loe(cursorId) : null;
	}

	private BooleanExpression likeName(String name) {
		return name != null ? product.name.like("%" + name + "%") : null;
	}

	private BooleanExpression eqStatus(ProductStatus status) {
		return status != null ? product.status.eq(status) : null;
	}
}
