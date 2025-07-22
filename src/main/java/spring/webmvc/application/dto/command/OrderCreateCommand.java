package spring.webmvc.application.dto.command;

import java.util.List;

public record OrderCreateCommand(
	List<OrderProductCreateCommand> products
) {
}
