package spring.webmvc.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MenuCreateRequest(
	@NotBlank
	String name,
	@NotBlank
	String path,
	Long parentId
) {
}
