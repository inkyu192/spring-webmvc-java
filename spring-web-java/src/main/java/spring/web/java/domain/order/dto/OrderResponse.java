package spring.web.java.domain.order.dto;

import java.time.LocalDateTime;
import java.util.List;

import spring.web.java.domain.Address;
import spring.web.java.domain.order.Order;

public record OrderResponse(
	Long id,
	String name,
	LocalDateTime orderDate,
	Order.Status status,
	Address address,
	List<OrderItemResponse> orderItems
) {
	public OrderResponse(Order order) {
		this(
			order.getId(),
			order.getMember().getName(),
			order.getOrderDate(),
			order.getStatus(),
			order.getMember().getAddress(),
			order.getOrderItems().stream()
				.map(OrderItemResponse::new)
				.toList()
		);
	}
}
