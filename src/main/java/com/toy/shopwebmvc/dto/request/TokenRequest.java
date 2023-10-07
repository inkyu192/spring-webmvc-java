package com.toy.shopwebmvc.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record TokenRequest(
        @NotEmpty
        String accessToken,
        @NotEmpty
        String refreshToken
) {
}
