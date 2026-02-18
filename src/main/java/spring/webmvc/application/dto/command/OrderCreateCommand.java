package spring.webmvc.application.dto.command;

import java.util.List;

public record OrderCreateCommand(
	Long userId,
	List<OrderProductCreateCommand> products
) {
}
