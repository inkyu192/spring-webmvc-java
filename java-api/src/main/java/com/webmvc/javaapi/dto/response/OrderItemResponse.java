package com.webmvc.javaapi.dto.response;


import com.webmvc.javaapi.domain.OrderItem;

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
