package com.toy.shopwebmvc.dto.response;

import com.toy.shopwebmvc.domain.OrderItem;

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
