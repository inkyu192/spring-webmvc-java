package spring.webmvc.presentation.dto.request;

import java.util.List;

import jakarta.validation.constraints.Size;
import spring.webmvc.application.dto.command.OrderCreateCommand;
import spring.webmvc.application.dto.command.OrderProductCreateCommand;

public record OrderCreateRequest(
	@Size(min = 1)
	List<OrderProductCreateRequest> products
) {
	public OrderCreateRequest {
		if (products == null) {
			products = List.of();
		}
	}

	public OrderCreateCommand toCommand() {
		return new OrderCreateCommand(
			products.stream()
				.map(it -> new OrderProductCreateCommand(it.id(), it.quantity()))
				.toList()
		);
	}
}
