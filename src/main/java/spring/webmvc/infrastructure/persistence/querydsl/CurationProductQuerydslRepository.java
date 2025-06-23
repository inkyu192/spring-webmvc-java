package spring.webmvc.infrastructure.persistence.querydsl;

import static spring.webmvc.domain.model.entity.QCurationProduct.*;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.CurationProduct;

@Repository
@RequiredArgsConstructor
public class CurationProductQuerydslRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public Page<CurationProduct> findAllByCurationId(Pageable pageable, Long curationId) {
		Long count = Objects.requireNonNullElse(
			jpaQueryFactory
				.select(curationProduct.count())
				.from(curationProduct)
				.where(eqCurationId(curationId))
				.fetchOne(), 0L
		);

		List<CurationProduct> content = jpaQueryFactory
			.selectFrom(curationProduct)
			.where(eqCurationId(curationId))
			.limit(pageable.getPageSize())
			.offset(pageable.getOffset())
			.orderBy(curationProduct.sortOrder.asc())
			.fetch();

		return new PageImpl<>(content, pageable, count);
	}

	private BooleanExpression eqCurationId(Long curationId) {
		if (curationId == null) {
			return null;
		}
		return curationProduct.curation.id.eq(curationId);
	}
}
