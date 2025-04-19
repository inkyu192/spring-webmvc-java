package spring.webmvc.presentation.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OrderCreateRequest(
	@NotNull
	Long memberId,
	@NotBlank
	String city,
	@NotBlank
	String street,
	@NotBlank
	String zipcode,
	@Size(min = 1)
	List<OrderProductCreateRequest> orderProducts
) {
	public OrderCreateRequest {
		if (orderProducts == null) {
			orderProducts = List.of();
		}
	}
}
