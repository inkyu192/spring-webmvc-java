package spring.webmvc.presentation.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import spring.webmvc.domain.model.enums.Category;

public record ItemSaveRequest(
	@NotBlank
	String name,
	@NotBlank
	String description,
	@Min(100)
	int price,
	@Max(9999)
	int quantity,
	@NotNull
	Category category
) {
}
