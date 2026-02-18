package spring.webmvc.presentation.dto.request;

import spring.webmvc.application.dto.command.CurationProductCreateCommand;

public record CurationProductCreateRequest(
	Long productId,
	Long sortOrder
) {
	public CurationProductCreateCommand toCommand() {
		return new CurationProductCreateCommand(productId, sortOrder);
	}
}
