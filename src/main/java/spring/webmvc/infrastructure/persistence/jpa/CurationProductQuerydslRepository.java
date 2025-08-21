package spring.webmvc.infrastructure.persistence.jpa;

import static spring.webmvc.domain.model.entity.QCuration.*;
import static spring.webmvc.domain.model.entity.QCurationProduct.*;
import static spring.webmvc.domain.model.entity.QProduct.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.CurationProduct;
import spring.webmvc.infrastructure.persistence.dto.CursorPage;

@Repository
@RequiredArgsConstructor
public class CurationProductQuerydslRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public CursorPage<CurationProduct> findAll(Long curationId, Long cursorId, Integer size) {
		List<CurationProduct> content = jpaQueryFactory
			.selectFrom(curationProduct)
			.join(curationProduct.curation, curation).fetchJoin()
			.join(curationProduct.product, product).fetchJoin()
			.where(eqCurationId(curationId), loeCurationProductId(cursorId))
			.orderBy(curationProduct.id.desc())
			.limit(size + 1)
			.fetch();

		return new CursorPage<>(content, size, CurationProduct::getId);
	}

	private BooleanExpression eqCurationId(Long curationId) {
		if (curationId == null) {
			return null;
		}
		return curationProduct.curation.id.eq(curationId);
	}

	private BooleanExpression loeCurationProductId(Long cursorId) {
		if (cursorId == null) {
			return null;
		}
		return curationProduct.id.loe(cursorId);
	}
}
