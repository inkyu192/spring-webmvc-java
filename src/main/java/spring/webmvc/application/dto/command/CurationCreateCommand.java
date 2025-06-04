package spring.webmvc.application.dto.command;

import java.util.List;

import spring.webmvc.presentation.dto.request.CurationCreateRequest;

public record CurationCreateCommand(
	String title,
	Boolean isExposed,
	long sortOrder,
	List<CurationProductCreateCommand> products
) {
	public CurationCreateCommand(CurationCreateRequest curationCreateRequest) {
		this(
			curationCreateRequest.title(),
			curationCreateRequest.isExposed(),
			curationCreateRequest.sortOrder(),
			curationCreateRequest.products().stream()
				.map(CurationProductCreateCommand::new)
				.toList()
		);
	}
}
