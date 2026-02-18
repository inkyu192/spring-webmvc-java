package spring.webmvc.presentation.dto.request;

import java.util.List;

import jakarta.validation.constraints.Size;
import spring.webmvc.application.dto.command.CurationCreateCommand;
import spring.webmvc.domain.model.enums.CurationCategory;

public record CurationCreateRequest(
	String title,
	CurationCategory category,
	Boolean isExposed,
	Long sortOrder,
	@Size(min = 1)
	List<CurationProductCreateRequest> products
) {
	public CurationCreateCommand toCommand() {
		return new CurationCreateCommand(
			title,
			category,
			isExposed,
			sortOrder,
			products.stream()
				.map(CurationProductCreateRequest::toCommand)
				.toList()
		);
	}
}
