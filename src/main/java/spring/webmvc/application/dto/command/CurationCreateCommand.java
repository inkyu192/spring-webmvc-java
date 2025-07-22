package spring.webmvc.application.dto.command;

import java.util.List;

public record CurationCreateCommand(
	String title,
	Boolean isExposed,
	long sortOrder,
	List<CurationProductCreateCommand> products
) {
}
