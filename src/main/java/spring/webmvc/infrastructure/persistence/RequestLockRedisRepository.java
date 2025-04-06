package spring.webmvc.infrastructure.persistence;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RequestLockRedisRepository {

	private final RedisTemplate<String, String> redisTemplate;

	private String createKey(Long memberId, String method, String uri) {
		return "request-lock:%s:%s:%s".formatted(memberId, method, uri);
	}

	public boolean setIfAbsent(Long memberId, String method, String uri) {
		String key = createKey(memberId, method, uri);

		Boolean result = redisTemplate.opsForValue().setIfAbsent(key, "1", Duration.ofSeconds(1));

		return Boolean.TRUE.equals(result);
	}
}
