package spring.webmvc.presentation.dto.request;

import spring.webmvc.application.dto.command.OrderStatusUpdateCommand;
import spring.webmvc.domain.model.enums.OrderStatus;

public record OrderStatusUpdateRequest(
	OrderStatus orderStatus
) {
	public OrderStatusUpdateCommand toCommand(Long id) {
		return new OrderStatusUpdateCommand(id, orderStatus);
	}
}
