package spring.webmvc.application.dto.result;

import java.time.Instant;
import java.util.List;

import spring.webmvc.domain.model.entity.Order;
import spring.webmvc.domain.model.enums.OrderStatus;

public record OrderDetailResult(
	Long id,
	String orderNumber,
	Instant orderedAt,
	OrderStatus status,
	List<OrderProductResult> products
) {
	public static OrderDetailResult of(Order order) {
		return new OrderDetailResult(
			order.getId(),
			order.getOrderNumber(),
			order.getOrderedAt(),
			order.getStatus(),
			order.getOrderProducts().stream()
				.map(OrderProductResult::of)
				.toList()
		);
	}
}
