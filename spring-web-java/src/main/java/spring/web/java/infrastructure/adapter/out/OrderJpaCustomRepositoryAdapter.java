package spring.web.java.infrastructure.adapter.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import spring.web.java.application.port.out.OrderJpaCustomRepositoryPort;
import spring.web.java.common.constant.DeliveryStatus;
import spring.web.java.common.constant.OrderStatus;
import spring.web.java.domain.Order;

@Repository
@RequiredArgsConstructor
public class OrderJpaCustomRepositoryAdapter implements OrderJpaCustomRepositoryPort {

	private final EntityManager entityManager;

	@Override
	public Page<Order> findAll(
		Pageable pageable, Long memberId, OrderStatus orderStatus, DeliveryStatus deliveryStatus
	) {
		String countJpql = """
			SELECT COUNT(o)
			FROM Order o
			JOIN o.member m
			JOIN o.delivery d
			WHERE 1 = 1
			""";

		String contentJpql = """
			SELECT o
			FROM Order o
			JOIN FETCH o.member m
			JOIN FETCH o.delivery d
			WHERE 1 = 1
			""";

		if (memberId != null) {
			countJpql += " AND m.id = :memberId";
			contentJpql += " AND m.id = :memberId";
		}

		if (orderStatus != null) {
			countJpql += " AND o.status = :orderStatus";
			contentJpql += " AND o.status = :orderStatus";
		}

		if (deliveryStatus != null) {
			countJpql += " AND d.status = :deliveryStatus";
			contentJpql += " AND d.status = :deliveryStatus";
		}

		TypedQuery<Long> countQuery = entityManager.createQuery(countJpql, Long.class);
		TypedQuery<Order> contentQuery = entityManager.createQuery(contentJpql, Order.class)
			.setFirstResult((int)pageable.getOffset())
			.setMaxResults(pageable.getPageSize());

		if (memberId != null) {
			countQuery.setParameter("memberId", memberId);
			contentQuery.setParameter("memberId", memberId);
		}

		if (orderStatus != null) {
			countQuery.setParameter("orderStatus", orderStatus);
			contentQuery.setParameter("orderStatus", orderStatus);
		}

		if (deliveryStatus != null) {
			countQuery.setParameter("deliveryStatus", deliveryStatus);
			contentQuery.setParameter("deliveryStatus", deliveryStatus);
		}

		return new PageImpl<>(contentQuery.getResultList(), pageable, countQuery.getSingleResult());
	}
}
