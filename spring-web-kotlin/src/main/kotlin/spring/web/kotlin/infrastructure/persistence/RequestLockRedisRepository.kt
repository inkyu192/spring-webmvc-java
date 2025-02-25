package spring.web.kotlin.infrastructure.persistence

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class RequestLockRedisRepository(
    private val redisTemplate: RedisTemplate<String, String>
) {
    fun setIfAbsent(memberId: Long, method: String, uri: String): Boolean {
        val result = redisTemplate.opsForValue()
            .setIfAbsent("request_lock", "$memberId:$method:$uri", Duration.ofSeconds(1))

        return result == true
    }
}