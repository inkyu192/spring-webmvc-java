package spring.web.java.dto.response;


import spring.web.java.constant.OrderStatus;
import spring.web.java.domain.Address;
import spring.web.java.domain.Order;

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
        this(
                order.getId(),
                order.getMember().getName(),
                order.getOrderDate(),
                order.getStatus(),
                order.getMember().getAddress(),
                order.getOrderItems().stream()
                        .map(OrderItemResponse::new)
                        .toList()
        );
    }
}
