package com.toy.shop.common.dto;

public record LoginRequest(
        String account,
        String password
) {
}
