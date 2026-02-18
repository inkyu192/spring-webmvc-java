package spring.webmvc.presentation.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import spring.webmvc.application.dto.command.OrderCreateCommand;

public record OrderCreateRequest(
	@NotEmpty
	List<OrderProductCreateRequest> products
) {
	public OrderCreateCommand toCommand(Long userId) {
		return new OrderCreateCommand(
			userId,
			products.stream()
				.map(OrderProductCreateRequest::toCommand)
				.toList()
		);
	}
}
