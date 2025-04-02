package spring.webmvc.presentation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemSaveRequest(
	@NotNull
	Long itemId,
	@Min(1)
	int count
) {
}
