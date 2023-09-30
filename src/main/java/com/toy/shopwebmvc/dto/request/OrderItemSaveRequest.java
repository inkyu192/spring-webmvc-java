package com.toy.shopwebmvc.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record OrderItemSaveRequest(
        @NotEmpty
        Long itemId,
        @NotNull
        int count
) {
}
