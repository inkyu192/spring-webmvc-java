package com.toy.shop.business.order.dto;

import com.toy.shop.domain.Address;
import com.toy.shop.domain.Order;
import com.toy.shop.domain.OrderStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDto {

    @Getter
    public static class Save {

        @NotNull
        private Long memberId;

        @NotEmpty
        private String city;

        @NotEmpty
        private String street;

        @NotEmpty
        private String zipcode;

        @NotNull
        private List<OrderItemDto.Save> orderItems;
    }

    @Getter
    public static class Response {

        private Long id;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus status;
        private Address address;
        private List<OrderItemDto.Response> orderItems;

        public Response(Order order) {
            this.id = order.getId();
            this.orderDate = order.getOrderDate();
            this.status = order.getStatus();
            this.name = order.getMember().getName();
            this.address = order.getMember().getAddress();
            this.orderItems = order.getOrderItems().stream()
                    .map(OrderItemDto.Response::new)
                    .toList();
        }
    }
}
