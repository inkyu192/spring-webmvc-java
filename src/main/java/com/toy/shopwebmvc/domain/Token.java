package com.toy.shopwebmvc.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "refresh", timeToLive = 604800)
public class Token {

    @Id
    private String account;
    private String refreshToken;

    public static Token create(String account, String refreshToken) {
        Token token = new Token();

        token.account = account;
        token.refreshToken = refreshToken;

        return token;
    }
}
