package spring.webmvc.application.dto.command;

import spring.webmvc.presentation.dto.request.OrderProductCreateRequest;

public record OrderProductCreateCommand(
	Long productId,
	int quantity
) {
	public OrderProductCreateCommand(OrderProductCreateRequest orderProductCreateRequest) {
		this(
			orderProductCreateRequest.productId(),
			orderProductCreateRequest.quantity()
		);
	}
}
