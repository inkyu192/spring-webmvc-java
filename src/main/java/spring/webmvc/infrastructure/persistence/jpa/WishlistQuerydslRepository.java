package spring.webmvc.infrastructure.persistence.jpa;

import static spring.webmvc.domain.model.entity.QWishlist.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import spring.webmvc.domain.dto.CursorPage;
import spring.webmvc.domain.model.entity.Wishlist;

@Repository
public class WishlistQuerydslRepository {

	private static final long DEFAULT_PAGE_SIZE = 20L;

	private final JPAQueryFactory jpaQueryFactory;

	public WishlistQuerydslRepository(JPAQueryFactory jpaQueryFactory) {
		this.jpaQueryFactory = jpaQueryFactory;
	}

	public Set<Long> findProductIdsByUserId(Long userId) {
		List<Long> productIds = jpaQueryFactory
			.select(wishlist.product.id)
			.from(wishlist)
			.where(wishlist.user.id.eq(userId))
			.fetch();

		return new HashSet<>(productIds);
	}

	public CursorPage<Wishlist> findAllByUserIdWithCursorPage(Long userId, Long cursorId) {
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(wishlist.user.id.eq(userId));

		if (cursorId != null) {
			builder.and(wishlist.id.lt(cursorId));
		}

		List<Wishlist> content = jpaQueryFactory
			.selectFrom(wishlist)
			.where(builder)
			.orderBy(wishlist.createdAt.desc(), wishlist.id.desc())
			.limit(DEFAULT_PAGE_SIZE + 1)
			.fetch();

		return CursorPage.create(content, DEFAULT_PAGE_SIZE, Wishlist::getId);
	}
}
