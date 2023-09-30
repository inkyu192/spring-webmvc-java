package com.toy.shopwebmvc.dto.request;

public record LoginRequest(
        String account,
        String password
) {
}
