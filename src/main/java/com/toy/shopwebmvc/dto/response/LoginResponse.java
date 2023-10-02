package com.toy.shopwebmvc.dto.response;

public record LoginResponse(
        String accessToken,
        String refreshToken
) {
}
