package spring.webmvc.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

public record WishlistCreateRequest(
	@NotNull
	Long productId
) {
}
