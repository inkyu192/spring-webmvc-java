package com.toy.shop.domain;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@Getter
@RedisHash
public class Token {

    @Id
    private Long id;
    private String refreshToken;
    private LocalDateTime createdDate;

    public static Token createToken(Long id, String refreshToken) {
        Token token = new Token();
        token.id = id;
        token.refreshToken = refreshToken;
        token.createdDate = LocalDateTime.now();

        return token;
    }
}
