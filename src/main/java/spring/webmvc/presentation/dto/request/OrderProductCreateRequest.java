package spring.webmvc.presentation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import spring.webmvc.application.dto.command.OrderProductCreateCommand;

public record OrderProductCreateRequest(
	@NotNull
	Long id,
	@Min(1)
	int quantity
) {
	public OrderProductCreateCommand toCommand() {
		return new OrderProductCreateCommand(id, quantity);
	}
}
