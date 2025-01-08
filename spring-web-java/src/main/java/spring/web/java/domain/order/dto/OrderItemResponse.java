package spring.web.java.domain.order.dto;

import spring.web.java.domain.order.OrderItem;

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
