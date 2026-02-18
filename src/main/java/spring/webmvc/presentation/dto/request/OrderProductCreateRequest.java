package spring.webmvc.presentation.dto.request;

import jakarta.validation.constraints.Min;
import spring.webmvc.application.dto.command.OrderProductCreateCommand;

public record OrderProductCreateRequest(
	Long id,
	@Min(1)
	Long quantity
) {
	public OrderProductCreateCommand toCommand() {
		return new OrderProductCreateCommand(id, quantity);
	}
}
