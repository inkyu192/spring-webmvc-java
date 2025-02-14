package spring.web.java.interfaces.dto.response;

import spring.web.java.domain.model.entity.OrderItem;

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
