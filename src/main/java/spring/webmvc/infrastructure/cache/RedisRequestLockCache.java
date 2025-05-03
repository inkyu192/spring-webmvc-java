package spring.webmvc.infrastructure.cache;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.cache.RequestLockCache;

@Component
@RequiredArgsConstructor
public class RedisRequestLockCache implements RequestLockCache {

	private final RedisTemplate<String, String> redisTemplate;

	private String createKey(Long memberId, String method, String uri) {
		return "request-lock:%s:%s:%s".formatted(memberId, method, uri);
	}

	@Override
	public boolean setIfAbsent(Long memberId, String method, String uri) {
		String key = createKey(memberId, method, uri);
		Boolean result = redisTemplate.opsForValue().setIfAbsent(key, "1", Duration.ofSeconds(1));

		return Boolean.TRUE.equals(result);
	}
}
