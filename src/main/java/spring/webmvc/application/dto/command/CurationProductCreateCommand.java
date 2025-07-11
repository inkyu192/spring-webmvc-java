package spring.webmvc.application.dto.command;

import spring.webmvc.presentation.dto.request.CurationProductCreateRequest;

public record CurationProductCreateCommand(
	Long productId,
	Integer sortOrder
) {
	public CurationProductCreateCommand(CurationProductCreateRequest curationProductCreateRequest) {
		this(
			curationProductCreateRequest.id(),
			curationProductCreateRequest.sortOrder()
		);
	}
}