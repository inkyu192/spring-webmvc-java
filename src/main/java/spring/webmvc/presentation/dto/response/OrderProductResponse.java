package spring.webmvc.presentation.dto.response;

import spring.webmvc.domain.model.entity.OrderProduct;

public record OrderProductResponse(
	String name,
	int price,
	int quantity
) {
	public OrderProductResponse(OrderProduct orderProduct) {
		this(
			orderProduct.getProduct().getName(),
			orderProduct.getOrderPrice(),
			orderProduct.getQuantity()
		);
	}
}
