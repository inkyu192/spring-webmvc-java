package spring.webmvc.application.dto.command;

import java.util.List;

import spring.webmvc.domain.model.enums.CurationCategory;

public record CurationCreateCommand(
	String title,
	CurationCategory category,
	Boolean isExposed,
	Long sortOrder,
	List<CurationProductCreateCommand> products
) {
}
