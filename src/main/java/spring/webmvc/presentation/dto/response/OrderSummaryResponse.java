package spring.webmvc.presentation.dto.response;

import java.time.Instant;

import spring.webmvc.application.dto.result.OrderSummaryResult;
import spring.webmvc.domain.model.enums.OrderStatus;

public record OrderSummaryResponse(
	Long id,
	Instant orderedAt,
	OrderStatus status
) {
	public static OrderSummaryResponse of(OrderSummaryResult result) {
		return new OrderSummaryResponse(
			result.id(),
			result.orderedAt(),
			result.status()
		);
	}
}
