package spring.webmvc.application.dto.command;

public record OrderProductCreateCommand(
	Long id,
	Long quantity
) {
}
