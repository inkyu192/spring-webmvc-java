package com.toy.shop.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record MemberLoginRequest(
        @NotEmpty
        String account,
        @NotEmpty
        String password
) {
}
