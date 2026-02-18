package spring.webmvc.presentation.dto.response;

import spring.webmvc.application.dto.result.OrderProductResult;

public record OrderProductResponse(
	Long id,
	String name,
	Long price,
	Long quantity
) {
	public static OrderProductResponse from(OrderProductResult result) {
		return new OrderProductResponse(
			result.id(),
			result.name(),
			result.price(),
			result.quantity()
		);
	}
}
