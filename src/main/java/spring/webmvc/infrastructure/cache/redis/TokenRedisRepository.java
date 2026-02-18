package spring.webmvc.infrastructure.cache.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.webmvc.domain.repository.cache.TokenCacheRepository;
import spring.webmvc.infrastructure.properties.AppProperties;

@Repository
@Slf4j
@RequiredArgsConstructor
public class TokenRedisRepository implements TokenCacheRepository {

	private static final String REFRESH_TOKEN_KEY = "user:%d:refresh-tokens";
	private static final int MAX_TOKENS = 3;

	private final RedisTemplate<String, String> redisTemplate;
	private final AppProperties appProperties;

	@Override
	public void addRefreshToken(Long userId, String refreshToken) {
		String key = String.format(REFRESH_TOKEN_KEY, userId);
		try {
			double score = System.currentTimeMillis();
			redisTemplate.opsForZSet().add(key, refreshToken, score);

			Long size = redisTemplate.opsForZSet().zCard(key);
			if (size != null && size > MAX_TOKENS) {
				redisTemplate.opsForZSet().removeRange(key, 0, size - MAX_TOKENS - 1);
			}

			long ttl = appProperties.jwt().refreshToken().expiration().getSeconds();
			redisTemplate.expire(key, ttl, TimeUnit.SECONDS);
		} catch (Exception e) {
			log.error("Failed to add refresh token for userId={}: {}", userId, e.getMessage(), e);
		}
	}

	@Override
	public String getRefreshToken(Long userId, String refreshToken) {
		String key = String.format(REFRESH_TOKEN_KEY, userId);

		try {
			Double score = redisTemplate.opsForZSet().score(key, refreshToken);

			return score != null ? refreshToken : null;
		} catch (Exception e) {
			log.warn("Failed to get refresh token for userId={}: {}", userId, e.getMessage());
			return null;
		}
	}

	@Override
	public void removeRefreshToken(Long userId, String refreshToken) {
		String key = String.format(REFRESH_TOKEN_KEY, userId);

		try {
			redisTemplate.opsForZSet().remove(key, refreshToken);
		} catch (Exception e) {
			log.error("Failed to remove refresh token for userId={}: {}", userId, e.getMessage(), e);
		}
	}
}
