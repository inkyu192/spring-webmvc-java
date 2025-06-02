package spring.webmvc.presentation.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CurationCreateRequest(
	@NotBlank
	String title,
	@NotNull
	Boolean isExposed,
	@NotNull
	long sortOrder,
	@Size(min = 1)
	List<CurationProductCreateRequest> products
) {
	public CurationCreateRequest {
		if (products == null) {
			products = List.of();
		}
	}
}
