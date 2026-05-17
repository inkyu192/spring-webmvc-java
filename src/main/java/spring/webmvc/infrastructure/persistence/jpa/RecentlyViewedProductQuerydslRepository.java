package spring.webmvc.infrastructure.persistence.jpa;

import static spring.webmvc.domain.model.entity.QRecentlyViewedProduct.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import spring.webmvc.domain.dto.CursorPage;
import spring.webmvc.domain.model.entity.RecentlyViewedProduct;

@Repository
public class RecentlyViewedProductQuerydslRepository {

	private static final long DEFAULT_PAGE_SIZE = 20L;

	private final JPAQueryFactory jpaQueryFactory;

	public RecentlyViewedProductQuerydslRepository(JPAQueryFactory jpaQueryFactory) {
		this.jpaQueryFactory = jpaQueryFactory;
	}

	public Set<Long> findProductIdsByUserIdWithinDays(Long userId, int days) {
		Instant since = Instant.now().minus(days, ChronoUnit.DAYS);

		List<Long> productIds = jpaQueryFactory
			.select(recentlyViewedProduct.product.id)
			.from(recentlyViewedProduct)
			.where(
				recentlyViewedProduct.user.id.eq(userId),
				recentlyViewedProduct.viewedAt.goe(since)
			)
			.fetch();

		return new HashSet<>(productIds);
	}

	public CursorPage<RecentlyViewedProduct> findAllByUserIdWithCursorPage(Long userId, Long cursorId, int days) {
		Instant since = Instant.now().minus(days, ChronoUnit.DAYS);

		BooleanBuilder builder = new BooleanBuilder();
		builder.and(recentlyViewedProduct.user.id.eq(userId));
		builder.and(recentlyViewedProduct.viewedAt.goe(since));

		if (cursorId != null) {
			builder.and(recentlyViewedProduct.id.lt(cursorId));
		}

		List<RecentlyViewedProduct> content = jpaQueryFactory
			.selectFrom(recentlyViewedProduct)
			.where(builder)
			.orderBy(recentlyViewedProduct.viewedAt.desc(), recentlyViewedProduct.id.desc())
			.limit(DEFAULT_PAGE_SIZE + 1)
			.fetch();

		return CursorPage.create(content, DEFAULT_PAGE_SIZE, RecentlyViewedProduct::getId);
	}
}
