package spring.webmvc.infrastructure.cache;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.cache.TicketCache;

@Component
@RequiredArgsConstructor
public class RedisTicketCache implements TicketCache {

	private final RedisTemplate<String, String> redisTemplate;

	private String createKey(Long productId) {
		return "product:ticket:%s".formatted(productId);
	}

	@Override
	public String get(Long productId) {
		try {
			return redisTemplate.opsForValue().get(createKey(productId));
		} catch (RuntimeException e) {
			return null;
		}
	}

	@Override
	public void set(Long productId, String value) {
		redisTemplate.opsForValue().set(createKey(productId), value, Duration.ofHours(1));
	}
}
