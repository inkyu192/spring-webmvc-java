package spring.web.kotlin.domain

import org.springframework.data.redis.core.RedisHash

@RedisHash(value = "token", timeToLive = 604800)
class Token private constructor(
    val account: String,
    val refreshToken: String
){
    companion object {
        fun create(account: String, refreshToken: String) {
            Token(
                account = account,
                refreshToken = refreshToken
            )
        }
    }
}