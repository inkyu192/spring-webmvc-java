package spring.webmvc.presentation.dto.response;

import java.time.Instant;
import java.util.List;

import spring.webmvc.domain.model.entity.Order;
import spring.webmvc.domain.model.enums.OrderStatus;

public record OrderResponse(
	Long id,
	Instant orderedAt,
	OrderStatus status,
	List<OrderProductResponse> products
) {
	public OrderResponse(Order order) {
		this(
			order.getId(),
			order.getOrderedAt(),
			order.getStatus(),
			order.getOrderProducts().stream()
				.map(OrderProductResponse::new)
				.toList()
		);
	}
}
