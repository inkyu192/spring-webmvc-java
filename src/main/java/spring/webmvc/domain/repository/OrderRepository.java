package spring.webmvc.domain.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import spring.webmvc.domain.model.entity.Order;
import spring.webmvc.domain.model.enums.OrderStatus;
import spring.webmvc.infrastructure.persistence.dto.CursorPage;

public interface OrderRepository {
	Page<Order> findAllWithOffsetPage(
		Pageable pageable,
		Long userId,
		OrderStatus orderStatus,
		Instant orderedFrom,
		Instant orderedTo
	);

	CursorPage<Order> findAllWithCursorPage(
		Long cursorId,
		Long userId,
		OrderStatus orderStatus,
		Instant orderedFrom,
		Instant orderedTo
	);

	Optional<Order> findById(Long id);

	Optional<Order> findByIdAndUserId(Long id, Long userId);

	Order save(Order order);
}
