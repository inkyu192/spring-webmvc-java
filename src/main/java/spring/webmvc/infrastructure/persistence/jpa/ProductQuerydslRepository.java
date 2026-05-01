package spring.webmvc.infrastructure.persistence.jpa;

import static spring.webmvc.domain.model.entity.QProduct.*;
import static spring.webmvc.domain.model.entity.QProductTag.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import spring.webmvc.application.dto.query.ProductCursorPageQuery;
import spring.webmvc.application.dto.query.ProductOffsetPageQuery;
import spring.webmvc.domain.dto.CursorPage;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.enums.ProductStatus;

@Repository
public class ProductQuerydslRepository {

	private static final long DEFAULT_PAGE_SIZE = 10L;

	private final JPAQueryFactory jpaQueryFactory;

	public ProductQuerydslRepository(JPAQueryFactory jpaQueryFactory) {
		this.jpaQueryFactory = jpaQueryFactory;
	}

	public CursorPage<Product> findAllWithCursorPage(ProductCursorPageQuery query) {
		JPAQuery<Product> baseQuery = jpaQueryFactory
			.selectFrom(product)
			.distinct();

		if (query.tagIds() != null && !query.tagIds().isEmpty()) {
			baseQuery.innerJoin(productTag).on(productTag.product.eq(product));
		}

		List<Product> content = baseQuery
			.where(
				loeProductId(query.cursorId()),
				likeName(query.name()),
				eqStatus(query.status()),
				inTagIds(query.tagIds())
			)
			.orderBy(product.id.desc())
			.limit(DEFAULT_PAGE_SIZE + 1)
			.fetch();

		return CursorPage.create(content, DEFAULT_PAGE_SIZE, Product::getId);
	}

	public Page<Product> findAllWithOffsetPage(ProductOffsetPageQuery query) {
		JPAQuery<Long> countQuery = jpaQueryFactory
			.select(product.countDistinct())
			.from(product);

		JPAQuery<Product> contentQuery = jpaQueryFactory
			.selectFrom(product)
			.distinct();

		if (query.tagIds() != null && !query.tagIds().isEmpty()) {
			countQuery.innerJoin(productTag).on(productTag.product.eq(product));
			contentQuery.innerJoin(productTag).on(productTag.product.eq(product));
		}

		Long count = countQuery
			.where(
				likeName(query.name()),
				eqStatus(query.status()),
				inTagIds(query.tagIds())
			)
			.fetchOne();

		if (count == null) {
			count = 0L;
		}

		List<Product> content = contentQuery
			.where(
				likeName(query.name()),
				eqStatus(query.status()),
				inTagIds(query.tagIds())
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

	private BooleanExpression inTagIds(List<Long> tagIds) {
		if (tagIds == null || tagIds.isEmpty()) {
			return null;
		}
		return productTag.tag.id.in(tagIds);
	}
}
