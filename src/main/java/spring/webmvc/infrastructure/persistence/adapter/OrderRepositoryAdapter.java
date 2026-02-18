package spring.webmvc.infrastructure.persistence.adapter;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Order;
import spring.webmvc.domain.model.enums.OrderStatus;
import spring.webmvc.domain.repository.OrderRepository;
import spring.webmvc.infrastructure.persistence.dto.CursorPage;
import spring.webmvc.infrastructure.persistence.jpa.OrderJpaRepository;
import spring.webmvc.infrastructure.persistence.jpa.OrderQuerydslRepository;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

	private final OrderJpaRepository jpaRepository;
	private final OrderQuerydslRepository querydslRepository;

	@Override
	public Page<Order> findAllWithOffsetPage(
		Pageable pageable,
		Long userId,
		OrderStatus orderStatus,
		Instant orderedFrom,
		Instant orderedTo
	) {
		return querydslRepository.findAllWithOffsetPage(pageable, userId, orderStatus, orderedFrom, orderedTo);
	}

	@Override
	public CursorPage<Order> findAllWithCursorPage(
		Long cursorId,
		Long userId,
		OrderStatus orderStatus,
		Instant orderedFrom,
		Instant orderedTo
	) {
		return querydslRepository.findAllWithCursorPage(cursorId, userId, orderStatus, orderedFrom, orderedTo);
	}

	@Override
	public Optional<Order> findById(Long id) {
		return jpaRepository.findById(id);
	}

	@Override
	public Optional<Order> findByIdAndUserId(Long id, Long userId) {
		return jpaRepository.findByIdAndUserId(id, userId);
	}

	@Override
	public Order save(Order order) {
		return jpaRepository.save(order);
	}
}
