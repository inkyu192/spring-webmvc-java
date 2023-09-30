package com.toy.shop.dto.request;

public record LoginRequest(
        String account,
        String password
) {
}
