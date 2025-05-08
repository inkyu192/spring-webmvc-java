package spring.webmvc.application.dto.command;

import java.util.List;

import spring.webmvc.presentation.dto.request.OrderCreateRequest;

public record OrderCreateCommand(
	List<OrderProductCreateCommand> products
) {
	public OrderCreateCommand(OrderCreateRequest orderCreateRequest) {
		this(
			orderCreateRequest.products().stream()
				.map(OrderProductCreateCommand::new)
				.toList()
		);
	}
}
