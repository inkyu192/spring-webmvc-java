package com.toy.shop.dto.response;

public record MemberLoginResponse(
        String accessToken,
        String refreshToken
) {
}