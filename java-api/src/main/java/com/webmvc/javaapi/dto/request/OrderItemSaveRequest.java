package com.webmvc.javaapi.dto.request;

import jakarta.validation.constraints.NotNull;

public record OrderItemSaveRequest(
        @NotNull
        Long itemId,
        int count
) {
}
