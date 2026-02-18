package spring.webmvc.presentation.dto.response;

import java.time.Instant;
import java.util.List;

import spring.webmvc.application.dto.result.OrderDetailResult;
import spring.webmvc.domain.model.enums.OrderStatus;

public record OrderDetailResponse(
	Long id,
	Instant orderedAt,
	OrderStatus status,
	List<OrderProductResponse> products
) {
	public static OrderDetailResponse of(OrderDetailResult result) {
		return new OrderDetailResponse(
			result.id(),
			result.orderedAt(),
			result.status(),
			result.products().stream()
				.map(OrderProductResponse::from)
				.toList()
		);
	}
}
