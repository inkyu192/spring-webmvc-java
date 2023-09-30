package com.toy.shop.dto.response;

import com.toy.shop.domain.Role;
import jakarta.validation.constraints.NotEmpty;

public record RoleResponse(
        @NotEmpty
        String id,
        @NotEmpty
        String name,
        @NotEmpty
        String description
) {
    public RoleResponse(Role role) {
        this(role.getId(), role.getName(), role.getDescription());
    }
}
