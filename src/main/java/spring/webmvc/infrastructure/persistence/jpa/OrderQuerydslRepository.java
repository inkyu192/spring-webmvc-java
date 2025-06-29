package spring.webmvc.infrastructure.persistence.jpa;

import static spring.webmvc.domain.model.entity.QMember.*;
import static spring.webmvc.domain.model.entity.QOrder.*;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Order;
import spring.webmvc.domain.model.enums.OrderStatus;

@Repository
@RequiredArgsConstructor
public class OrderQuerydslRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public Page<Order> findAll(Pageable pageable, Long memberId, OrderStatus orderStatus) {
		long count = Objects.requireNonNullElse(
			jpaQueryFactory
				.select(order.count())
				.from(order)
				.join(order.member, member)
				.where(eqMemberId(memberId), eqOrderStatus(orderStatus))
				.fetchOne(), 0L
		);

		List<Order> content = jpaQueryFactory
			.selectFrom(order)
			.join(order.member, member).fetchJoin()
			.where(eqMemberId(memberId), eqOrderStatus(orderStatus))
			.limit(pageable.getPageSize())
			.offset(pageable.getOffset())
			.fetch();

		return new PageImpl<>(content, pageable, count);
	}

	private BooleanExpression eqMemberId(Long memberId) {
		if (memberId == null) {
			return null;
		}
		return member.id.eq(memberId);
	}

	private BooleanExpression eqOrderStatus(OrderStatus orderStatus) {
		if (orderStatus == null) {
			return null;
		}
		return order.status.eq(orderStatus);
	}
}
