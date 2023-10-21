package com.toy.shopwebmvc.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "refresh", timeToLive = 604800)
public class Token {

    @Id
    private String account;
    private String refreshToken;

    @Builder(builderMethodName = "create")
    public Token(String account, String refreshToken) {
        this.account = account;
        this.refreshToken = refreshToken;
    }
}
