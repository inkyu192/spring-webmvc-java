package com.toy.shopwebmvc.dto.response;

import com.toy.shopwebmvc.domain.Address;
import com.toy.shopwebmvc.domain.Order;
import com.toy.shopwebmvc.constant.OrderStatus;

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
