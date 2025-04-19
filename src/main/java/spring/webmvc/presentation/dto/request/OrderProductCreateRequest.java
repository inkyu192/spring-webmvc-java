package spring.webmvc.presentation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderProductCreateRequest(
	@NotNull
	Long productId,
	@Min(1)
	int count
) {
}
