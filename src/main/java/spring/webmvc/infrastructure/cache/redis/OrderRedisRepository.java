package spring.webmvc.infrastructure.cache.redis;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.webmvc.domain.repository.cache.OrderCacheRepository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class OrderRedisRepository implements OrderCacheRepository {

	private static final String ORDER_SEQUENCE_KEY = "order:sequence:%s";

	private final RedisTemplate<String, String> redisTemplate;

	@Override
	public Long incrementSequence(String date) {
		String key = String.format(ORDER_SEQUENCE_KEY, date);
		try {
			Long seq = redisTemplate.opsForValue().increment(key);
			if (seq != null && seq == 1L) {
				redisTemplate.expire(key, Duration.ofDays(2));
			}
			return seq;
		} catch (Exception e) {
			log.warn("Redis increment failed: {}", e.getMessage());
			return null;
		}
	}

	@Override
	public boolean setSequence(String date, Long value) {
		String key = String.format(ORDER_SEQUENCE_KEY, date);
		try {
			redisTemplate.opsForValue().set(key, value.toString());
			redisTemplate.expire(key, Duration.ofDays(2));
			return true;
		} catch (Exception e) {
			log.warn("Redis set failed: {}", e.getMessage());
			return false;
		}
	}
}
