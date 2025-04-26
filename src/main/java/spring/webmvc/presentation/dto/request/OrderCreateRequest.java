package spring.webmvc.presentation.dto.request;

import java.util.List;

import jakarta.validation.constraints.Size;

public record OrderCreateRequest(
	@Size(min = 1)
	List<OrderProductCreateRequest> products
) {
	public OrderCreateRequest {
		if (products == null) {
			products = List.of();
		}
	}
}
