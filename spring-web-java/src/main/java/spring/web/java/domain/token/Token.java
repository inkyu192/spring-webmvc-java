package spring.web.java.domain.token;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "token", timeToLive = 604800)
public class Token {

    @Id
    private Long memberId;
    private String refreshToken;

    public static Token create(Long memberId, String refreshToken) {
        Token token = new Token();

        token.memberId = memberId;
        token.refreshToken = refreshToken;

        return token;
    }
}
