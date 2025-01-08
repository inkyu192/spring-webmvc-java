package spring.web.java.domain.order.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderSaveRequest(
        @NotNull
        Long memberId,
        @NotNull
        String city,
        @NotNull
        String street,
        @NotNull
        String zipcode,
        @NotNull
        List<OrderItemSaveRequest> orderItems
) {
}
