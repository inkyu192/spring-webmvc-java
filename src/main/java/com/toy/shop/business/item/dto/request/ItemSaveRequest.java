package com.toy.shop.business.item.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ItemSaveRequest(
        @NotEmpty
        String name,
        @NotEmpty
        String description,
        @NotNull
        @Min(100)
        int price,
        @NotNull
        @Max(value = 9999)
        int quantity,
        @NotNull
        Long categoryId
) {
}
