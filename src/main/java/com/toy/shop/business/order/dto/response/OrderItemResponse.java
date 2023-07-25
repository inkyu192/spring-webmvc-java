package com.toy.shop.business.order.dto.response;

import com.toy.shop.domain.OrderItem;

public record OrderItemResponse(
        String itemName,
        int orderPrice,
        int count
) {
    public OrderItemResponse(OrderItem orderItem) {
        this(orderItem.getItem().getName(), orderItem.getOrderPrice(), orderItem.getCount());
    }
}
