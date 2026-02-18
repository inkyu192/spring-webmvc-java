package spring.webmvc.application.dto.result;

import spring.webmvc.domain.model.entity.OrderProduct;

public record OrderProductResult(
	Long id,
	String name,
	Long price,
	Long quantity
) {
	public static OrderProductResult of(OrderProduct orderProduct) {
		return new OrderProductResult(
			orderProduct.getProduct().getId(),
			orderProduct.getProduct().getName(),
			orderProduct.getProduct().getPrice(),
			orderProduct.getQuantity()
		);
	}
}
