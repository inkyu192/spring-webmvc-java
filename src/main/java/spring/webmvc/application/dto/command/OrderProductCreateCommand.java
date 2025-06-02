package spring.webmvc.application.dto.command;

import spring.webmvc.presentation.dto.request.OrderProductCreateRequest;

public record OrderProductCreateCommand(
	Long id,
	int quantity
) {
	public OrderProductCreateCommand(OrderProductCreateRequest orderProductCreateRequest) {
		this(
			orderProductCreateRequest.id(),
			orderProductCreateRequest.quantity()
		);
	}
}
