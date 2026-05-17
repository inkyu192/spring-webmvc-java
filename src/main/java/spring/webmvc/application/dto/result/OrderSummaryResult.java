package spring.webmvc.application.dto.result;

import java.time.Instant;

import spring.webmvc.domain.model.entity.Order;
import spring.webmvc.domain.model.enums.OrderStatus;

public record OrderSummaryResult(
	Long id,
	String orderNumber,
	Instant orderedAt,
	OrderStatus status
) {
	public static OrderSummaryResult of(Order order) {
		return new OrderSummaryResult(
			order.getId(),
			order.getOrderNumber(),
			order.getOrderedAt(),
			order.getStatus()
		);
	}
}
