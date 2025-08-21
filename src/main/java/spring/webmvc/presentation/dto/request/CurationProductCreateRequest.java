package spring.webmvc.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import spring.webmvc.application.dto.command.CurationProductCreateCommand;

public record CurationProductCreateRequest(
	@NotNull
	Long id
) {
	public CurationProductCreateCommand toCommand() {
		return new CurationProductCreateCommand(id);
	}
}