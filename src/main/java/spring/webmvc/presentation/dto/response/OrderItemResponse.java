package spring.webmvc.presentation.dto.response;

import spring.webmvc.domain.model.entity.OrderItem;

public record OrderItemResponse(
	String itemName,
	int orderPrice,
	int count
) {
	public OrderItemResponse(OrderItem orderItem) {
		this(
			orderItem.getItem().getName(),
			orderItem.getOrderPrice(),
			orderItem.getCount()
		);
	}
}
