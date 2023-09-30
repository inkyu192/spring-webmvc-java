package com.toy.shop.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderSaveRequest(
        @NotNull
        Long memberId,
        @NotEmpty
        String city,
        @NotEmpty
        String street,
        @NotEmpty
        String zipcode,
        @NotNull
        List<OrderItemSaveRequest> orderItems
) {
}
