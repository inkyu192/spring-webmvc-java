package com.toy.shop.business.member.dto.response;

public record MemberLoginResponse(
        String accessToken,
        String refreshToken
) {
}