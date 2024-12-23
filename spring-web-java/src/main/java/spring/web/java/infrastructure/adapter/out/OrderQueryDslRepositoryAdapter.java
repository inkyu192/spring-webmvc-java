package spring.web.java.infrastructure.adapter.out;

import static spring.web.java.domain.QDelivery.*;
import static spring.web.java.domain.QMember.*;
import static spring.web.java.domain.QOrder.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import spring.web.java.application.port.out.OrderQueryDslRepositoryPort;
import spring.web.java.common.constant.DeliveryStatus;
import spring.web.java.common.constant.OrderStatus;
import spring.web.java.domain.Order;

@Repository
public class OrderQueryDslRepositoryAdapter implements OrderQueryDslRepositoryPort {

	private final JPAQueryFactory queryFactory;

	public OrderQueryDslRepositoryAdapter(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	@Override
	public Page<Order> findAll(
		Pageable pageable, Long memberId, OrderStatus orderStatus, DeliveryStatus deliveryStatus
	) {
		int count = queryFactory
			.selectOne()
			.from(order)
			.join(order.member, member)
			.join(order.delivery, delivery)
			.where(
				memberId != null ? member.id.eq(memberId) : null,
				orderStatus != null ? order.status.eq(orderStatus) : null,
				deliveryStatus != null ? delivery.status.eq(deliveryStatus) : null
			)
			.fetch()
			.size();

		List<Order> content = queryFactory
			.select(order)
			.from(order)
			.join(order.member, member)
			.fetchJoin()
			.join(order.delivery, delivery)
			.fetchJoin()
			.where(
				memberId != null ? member.id.eq(memberId) : null,
				orderStatus != null ? order.status.eq(orderStatus) : null,
				deliveryStatus != null ? delivery.status.eq(deliveryStatus) : null
			)
			.limit(pageable.getPageSize())
			.offset(pageable.getOffset())
			.fetch();

		return new PageImpl<>(content, pageable, count);
	}
}
