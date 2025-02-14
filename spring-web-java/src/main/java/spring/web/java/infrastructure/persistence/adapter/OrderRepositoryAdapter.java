package spring.web.java.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import spring.web.java.domain.model.entity.Order;
import spring.web.java.domain.model.enums.DeliveryStatus;
import spring.web.java.domain.model.enums.OrderStatus;
import spring.web.java.domain.repository.OrderRepository;
import spring.web.java.infrastructure.persistence.OrderJpaRepository;
import spring.web.java.infrastructure.persistence.OrderQuerydslRepository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

	private final OrderJpaRepository jpaRepository;
	private final OrderQuerydslRepository querydslRepository;

	@Override
	public Page<Order> findAll(
		Pageable pageable, Long memberId, OrderStatus orderStatus, DeliveryStatus deliveryStatus
	) {
		return querydslRepository.findAll(pageable, memberId, orderStatus, deliveryStatus);
	}

	@Override
	public Optional<Order> findById(Long id) {
		return jpaRepository.findById(id);
	}

	@Override
	public Order save(Order order) {
		return jpaRepository.save(order);
	}
}
