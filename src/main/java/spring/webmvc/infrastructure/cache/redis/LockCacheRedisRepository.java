package spring.webmvc.infrastructure.cache.redis;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.repository.LockCacheRepository;

@Component
@RequiredArgsConstructor
public class LockCacheRedisRepository implements LockCacheRepository {

	private final RedisTemplate<String, String> redisTemplate;

	@Override
	public Boolean tryLock(String method, String uri, String hash) {
		return redisTemplate.opsForValue()
			.setIfAbsent(
				"request-lock:$method:$uri:$hash",
				"1",
				Duration.ofSeconds(1)
			);
	}
}
