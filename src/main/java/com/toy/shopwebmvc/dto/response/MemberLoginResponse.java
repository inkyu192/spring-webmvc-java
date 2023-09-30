package com.toy.shopwebmvc.dto.response;

public record MemberLoginResponse(
        String accessToken,
        String refreshToken
) {
}