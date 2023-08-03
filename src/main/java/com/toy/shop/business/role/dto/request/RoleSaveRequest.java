package com.toy.shop.business.role.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record RoleSaveRequest(
        @NotEmpty
        String id,
        @NotEmpty
        String name,
        @NotEmpty
        String description
) {
}
