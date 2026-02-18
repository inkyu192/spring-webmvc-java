package spring.webmvc.infrastructure.cache.redis;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.webmvc.domain.repository.cache.RequestLockCacheRepository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class RequestLockRedisRepository implements RequestLockCacheRepository {

	private static final String REQUEST_LOCK_KEY = "request-lock:%s:%s:%s";
	private static final Duration LOCK_TIMEOUT = Duration.ofSeconds(1);

	private final RedisTemplate<String, String> redisTemplate;

	@Override
	public boolean tryLock(String method, String uri, String hash) {
		String key = String.format(REQUEST_LOCK_KEY, method, uri, hash);

		try {
			Boolean result = redisTemplate.opsForValue().setIfAbsent(key, "1", LOCK_TIMEOUT);

			return result != null && result;
		} catch (Exception e) {
			String message = "Failed to acquire request lock for method={}, uri={}, hash={}: {}";
			log.error(message, method, uri, hash, e.getMessage(), e);

			return false;
		}
	}
}
