package com.toy.shopwebmvc.dto.response;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
