package com.toy.shop.business.order.dto.response;

import com.toy.shop.domain.Address;
import com.toy.shop.domain.Order;
import com.toy.shop.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String name,
        LocalDateTime orderDate,
        OrderStatus status,
        Address address,
        List<OrderItemResponse> orderItems
) {
    public OrderResponse(Order order) {
        this(order.getId(), order.getMember().getName(), order.getOrderDate(), order.getStatus(),
                order.getMember().getAddress(), order.getOrderItems().stream().map(OrderItemResponse::new).toList());
    }
}
