package spring.web.kotlin.domain.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash(value = "token", timeToLive = 604800)
class Token private constructor(
    @Id
    val memberId: Long,
    val refreshToken: String
) {
    companion object {
        fun create(memberId: Long, refreshToken: String) =
            Token(
                memberId = memberId,
                refreshToken = refreshToken
            )
    }
}