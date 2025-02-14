package spring.web.java.infrastructure.persistence;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RequestLockRedisRepository {

	private final RedisTemplate<String, String> redisTemplate;

	public boolean setIfAbsent(Long memberId, String method, String uri) {
		Boolean result = redisTemplate.opsForValue()
			.setIfAbsent("request_lock", String.format("%s:%s:%s", memberId, method, uri), Duration.ofSeconds(1));

		return Boolean.TRUE.equals(result);
	}
}
