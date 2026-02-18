package spring.webmvc.infrastructure.persistence.jpa;

import static spring.webmvc.domain.model.entity.QOrder.*;
import static spring.webmvc.domain.model.entity.QUser.*;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import spring.webmvc.domain.model.entity.Order;
import spring.webmvc.domain.model.enums.OrderStatus;
import spring.webmvc.infrastructure.persistence.dto.CursorPage;

@Repository
public class OrderQuerydslRepository {

	private static final long DEFAULT_PAGE_SIZE = 10L;

	private final JPAQueryFactory jpaQueryFactory;

	public OrderQuerydslRepository(JPAQueryFactory jpaQueryFactory) {
		this.jpaQueryFactory = jpaQueryFactory;
	}

	public Page<Order> findAllWithOffsetPage(
		Pageable pageable,
		Long userId,
		OrderStatus orderStatus,
		Instant orderedFrom,
		Instant orderedTo
	) {
		Long count = jpaQueryFactory
			.select(order.count())
			.from(order)
			.join(order.user, user)
			.where(
				eqUserId(userId),
				eqOrderStatus(orderStatus),
				goeOrderedAt(orderedFrom),
				loeOrderedAt(orderedTo)
			)
			.fetchOne();

		if (count == null) {
			count = 0L;
		}

		List<Order> content = jpaQueryFactory
			.selectFrom(order)
			.join(order.user, user).fetchJoin()
			.where(
				eqUserId(userId),
				eqOrderStatus(orderStatus),
				goeOrderedAt(orderedFrom),
				loeOrderedAt(orderedTo)
			)
			.limit(pageable.getPageSize())
			.offset(pageable.getOffset())
			.fetch();

		return new PageImpl<>(content, pageable, count);
	}

	public CursorPage<Order> findAllWithCursorPage(
		Long cursorId,
		Long userId,
		OrderStatus orderStatus,
		Instant orderedFrom,
		Instant orderedTo
	) {
		List<Order> content = jpaQueryFactory
			.selectFrom(order)
			.join(order.user, user).fetchJoin()
			.where(
				loeOrderId(cursorId),
				eqUserId(userId),
				eqOrderStatus(orderStatus),
				goeOrderedAt(orderedFrom),
				loeOrderedAt(orderedTo)
			)
			.limit(DEFAULT_PAGE_SIZE + 1)
			.fetch();

		return CursorPage.create(content, DEFAULT_PAGE_SIZE, Order::getId);
	}

	private BooleanExpression loeOrderId(Long cursorId) {
		return cursorId != null ? order.id.loe(cursorId) : null;
	}

	private BooleanExpression eqUserId(Long userId) {
		return userId != null ? order.user.id.eq(userId) : null;
	}

	private BooleanExpression eqOrderStatus(OrderStatus orderStatus) {
		return orderStatus != null ? order.status.eq(orderStatus) : null;
	}

	private BooleanExpression goeOrderedAt(Instant orderedFrom) {
		return orderedFrom != null ? order.orderedAt.goe(orderedFrom) : null;
	}

	private BooleanExpression loeOrderedAt(Instant orderedTo) {
		return orderedTo != null ? order.orderedAt.loe(orderedTo) : null;
	}
}
