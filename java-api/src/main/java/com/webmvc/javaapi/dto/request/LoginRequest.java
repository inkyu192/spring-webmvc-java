package com.webmvc.javaapi.dto.request;

public record LoginRequest(
        String account,
        String password
) {
}
