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
	public void set(String key, String value) {
		redisTemplate.opsForValue().set(key, value);
	}

	@Override
	public void set(String key, String value, Duration timeout) {
		redisTemplate.opsForValue().set(key, value, timeout);
	}

	@Override
	public boolean setIfAbsent(String key, String value, Duration timeout) {
		Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value, timeout);
		return Boolean.TRUE.equals(result);
	}

	@Override
	public boolean delete(String key) {
		return redisTemplate.delete(key);
	}

	@Override
	public Long increment(String key, long delta) {
		return redisTemplate.opsForValue().increment(key, delta);
	}

	@Override
	public Long decrement(String key, long delta) {
		return redisTemplate.opsForValue().decrement(key, delta);
	}
}
