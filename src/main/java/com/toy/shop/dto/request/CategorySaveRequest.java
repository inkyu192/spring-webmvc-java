package com.toy.shop.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record CategorySaveRequest(
        @NotEmpty
        String name,
        @NotEmpty
        String description
) {
}
