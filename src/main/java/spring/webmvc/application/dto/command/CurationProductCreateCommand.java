package spring.webmvc.application.dto.command;

public record CurationProductCreateCommand(
	Long productId,
	Integer sortOrder
) {
}