package spring.webmvc.presentation.dto.response;

import java.time.Instant;
import java.util.List;

import spring.webmvc.domain.model.entity.Order;
import spring.webmvc.domain.model.enums.OrderStatus;

public record OrderResponse(
	Long id,
	String name,
	Instant orderedAt,
	OrderStatus status,
	List<OrderItemResponse> orderItems
) {
	public OrderResponse(Order order) {
		this(
			order.getId(),
			order.getMember().getName(),
			order.getOrderedAt(),
			order.getStatus(),
			order.getOrderItems().stream()
				.map(OrderItemResponse::new)
				.toList()
		);
	}
}
