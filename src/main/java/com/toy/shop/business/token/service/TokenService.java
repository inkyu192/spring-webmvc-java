package com.toy.shop.business.token.service;

import org.springframework.security.core.Authentication;

public interface TokenService {

    String createAccessToken(Authentication authentication);

    String createRefreshToken(Authentication authentication);

    Authentication getAuthentication(String token);
}
