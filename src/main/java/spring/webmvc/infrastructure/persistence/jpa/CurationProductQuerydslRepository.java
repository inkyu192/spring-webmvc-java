package spring.webmvc.infrastructure.persistence.jpa;

import static spring.webmvc.domain.model.entity.QCuration.*;
import static spring.webmvc.domain.model.entity.QCurationProduct.*;
import static spring.webmvc.domain.model.entity.QProduct.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import spring.webmvc.domain.model.entity.CurationProduct;
import spring.webmvc.infrastructure.persistence.dto.CursorPage;

@Repository
public class CurationProductQuerydslRepository {

	private static final long DEFAULT_PAGE_SIZE = 10L;

	private final JPAQueryFactory jpaQueryFactory;

	public CurationProductQuerydslRepository(JPAQueryFactory jpaQueryFactory) {
		this.jpaQueryFactory = jpaQueryFactory;
	}

	public CursorPage<CurationProduct> findAllWithCursorPage(Long curationId, Long cursorId) {
		List<CurationProduct> content = jpaQueryFactory
			.selectFrom(curationProduct)
			.join(curationProduct.curation, curation).fetchJoin()
			.join(curationProduct.product, product).fetchJoin()
			.where(
				eqCurationId(curationId),
				loeCurationProductId(cursorId)
			)
			.orderBy(curationProduct.id.desc())
			.limit(DEFAULT_PAGE_SIZE + 1)
			.fetch();

		return CursorPage.create(content, DEFAULT_PAGE_SIZE, CurationProduct::getId);
	}

	public Page<CurationProduct> findAllWithOffsetPage(Long curationId, Pageable pageable) {
		List<CurationProduct> content = jpaQueryFactory
			.selectFrom(curationProduct)
			.join(curationProduct.curation, curation).fetchJoin()
			.join(curationProduct.product, product).fetchJoin()
			.where(eqCurationId(curationId))
			.orderBy(curationProduct.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(curationProduct.count())
			.from(curationProduct)
			.where(eqCurationId(curationId))
			.fetchOne();

		if (total == null) {
			total = 0L;
		}

		return new PageImpl<>(content, pageable, total);
	}

	private BooleanExpression eqCurationId(Long curationId) {
		return curationId != null ? curationProduct.curation.id.eq(curationId) : null;
	}

	private BooleanExpression loeCurationProductId(Long cursorId) {
		return cursorId != null ? curationProduct.id.loe(cursorId) : null;
	}
}
