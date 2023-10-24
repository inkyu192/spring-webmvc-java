package com.toy.shopwebmvc.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record TokenRequest(
        @NotNull
        String accessToken,
        @NotNull
        String refreshToken
) {
}
