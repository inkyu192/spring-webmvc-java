package spring.webmvc.infrastructure.cache;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.cache.AccommodationCache;

@Component
@RequiredArgsConstructor
public class RedisAccommodationCache implements AccommodationCache {

	private final RedisTemplate<String, String> redisTemplate;

	private String createKey(Long productId) {
		return "product:accommodation:%s".formatted(productId);
	}

	@Override
	public String get(Long id) {
		try {
			return redisTemplate.opsForValue().get(createKey(id));
		} catch (RuntimeException e) {
			return null;
		}
	}

	@Override
	public void set(Long id, String value) {
		redisTemplate.opsForValue().set(createKey(id), value, Duration.ofHours(1));
	}
}