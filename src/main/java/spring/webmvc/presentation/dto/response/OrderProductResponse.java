package spring.webmvc.presentation.dto.response;

import spring.webmvc.domain.model.entity.OrderProduct;

public record OrderProductResponse(
	String name,
	long price,
	long quantity
) {
	public OrderProductResponse(OrderProduct orderProduct) {
		this(
			orderProduct.getProduct().getName(),
			orderProduct.getOrderPrice(),
			orderProduct.getQuantity()
		);
	}
}
