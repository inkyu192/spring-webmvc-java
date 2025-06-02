package spring.webmvc.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

public record CurationProductCreateRequest(
	@NotNull
	Long id,
	@NotNull
	Integer sortOrder
) {
}