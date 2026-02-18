package spring.webmvc.application.dto.command;

import spring.webmvc.domain.model.enums.OrderStatus;

public record OrderStatusUpdateCommand(
	Long id,
	OrderStatus orderStatus
) {
}
