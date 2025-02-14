package spring.web.java.interfaces.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import spring.web.java.domain.model.entity.Address;
import spring.web.java.domain.model.entity.Order;
import spring.web.java.domain.model.enums.OrderStatus;

public record OrderResponse(
	Long id,
	String name,
	LocalDateTime orderDate,
	OrderStatus status,
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
