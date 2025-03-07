package spring.web.java.presentation.dto.response;

import java.time.Instant;
import java.util.List;

import spring.web.java.domain.model.entity.Address;
import spring.web.java.domain.model.entity.Order;
import spring.web.java.domain.model.enums.OrderStatus;

public record OrderResponse(
	Long id,
	String name,
	Instant orderedAt,
	OrderStatus status,
	Address address,
	List<OrderItemResponse> orderItems
) {
	public OrderResponse(Order order) {
		this(
			order.getId(),
			order.getMember().getName(),
			order.getOrderedAt(),
			order.getStatus(),
			order.getMember().getAddress(),
			order.getOrderItems().stream()
				.map(OrderItemResponse::new)
				.toList()
		);
	}
}
