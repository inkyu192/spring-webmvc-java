package spring.webmvc.application.dto.command;

public record OrderProductCreateCommand(
	Long id,
	int quantity
) {
}
