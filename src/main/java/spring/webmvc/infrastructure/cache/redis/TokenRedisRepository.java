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

	private static final String REFRESH_TOKEN_KEY = "user:%d:device:%s:refresh-token";

	private final RedisTemplate<String, String> redisTemplate;
	private final AppProperties appProperties;

	@Override
	public void setRefreshToken(Long userId, String deviceId, String refreshToken) {
		String key = String.format(REFRESH_TOKEN_KEY, userId, deviceId);
		try {
			long ttl = appProperties.jwt().refreshToken().expiration().getSeconds();
			redisTemplate.opsForValue().set(key, refreshToken, ttl, TimeUnit.SECONDS);
		} catch (Exception e) {
			log.error("Failed to set refresh token for userId={}, deviceId={}: {}", userId, deviceId, e.getMessage(),
				e);
		}
	}

	@Override
	public String getRefreshToken(Long userId, String deviceId) {
		String key = String.format(REFRESH_TOKEN_KEY, userId, deviceId);
		try {
			return redisTemplate.opsForValue().get(key);
		} catch (Exception e) {
			log.warn("Failed to get refresh token for userId={}, deviceId={}: {}", userId, deviceId, e.getMessage());
			return null;
		}
	}

	@Override
	public void removeRefreshToken(Long userId, String deviceId) {
		String key = String.format(REFRESH_TOKEN_KEY, userId, deviceId);
		try {
			redisTemplate.delete(key);
		} catch (Exception e) {
			log.error("Failed to remove refresh token for userId={}, deviceId={}: {}", userId, deviceId, e.getMessage(),
				e);
		}
	}
}
