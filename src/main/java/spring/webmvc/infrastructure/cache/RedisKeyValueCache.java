package spring.webmvc.infrastructure.cache;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.cache.KeyValueCache;

@Component
@RequiredArgsConstructor
public class RedisKeyValueCache implements KeyValueCache {

	private final RedisTemplate<String, String> redisTemplate;

	@Override
	public String get(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	@Override
	public void set(String key, String value, Duration duration) {
		redisTemplate.opsForValue().set(key, value, duration);
	}

	@Override
	public boolean setIfAbsent(String key, String value, Duration duration) {
		Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value, duration);
		return Boolean.TRUE.equals(result);
	}
}
