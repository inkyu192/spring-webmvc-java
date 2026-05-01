package spring.webmvc.infrastructure.cache.redis;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import spring.webmvc.infrastructure.config.CacheTest;
import spring.webmvc.infrastructure.properties.AppProperties;

@CacheTest
class TokenRedisRepositoryTest {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	private final AppProperties appProperties = mock(AppProperties.class);
	private TokenRedisRepository repository;

	@BeforeEach
	void setUp() {
		repository = new TokenRedisRepository(redisTemplate, appProperties);
	}

	@Test
	@DisplayName("setRefreshToken: refresh token을 저장한다")
	void setRefreshToken() {
		Long userId = 1L;
		String deviceId = "device-1";
		String refreshToken = "test-refresh-token";

		AppProperties.JwtProperties.TokenProperties tokenProperties = mock(
			AppProperties.JwtProperties.TokenProperties.class);
		AppProperties.JwtProperties jwtProperties = mock(AppProperties.JwtProperties.class);
		when(appProperties.jwt()).thenReturn(jwtProperties);
		when(jwtProperties.refreshToken()).thenReturn(tokenProperties);
		when(tokenProperties.expiration()).thenReturn(Duration.ofSeconds(60));

		repository.setRefreshToken(userId, deviceId, refreshToken);

		String stored = redisTemplate.opsForValue().get("user:" + userId + ":device:" + deviceId + ":refresh-token");
		assertThat(stored).isEqualTo(refreshToken);
	}

	@Test
	@DisplayName("getRefreshToken: 저장된 refresh token을 조회한다")
	void getRefreshToken() {
		Long userId = 1L;
		String deviceId = "device-1";
		String refreshToken = "test-refresh-token";
		redisTemplate.opsForValue().set("user:" + userId + ":device:" + deviceId + ":refresh-token", refreshToken);

		String result = repository.getRefreshToken(userId, deviceId);

		assertThat(result).isEqualTo(refreshToken);
	}

	@Test
	@DisplayName("getRefreshToken: 존재하지 않는 키 조회 시 null을 반환한다")
	void getRefreshTokenNotFound() {
		String result = repository.getRefreshToken(999L, "non-existent-device");

		assertThat(result).isNull();
	}

	@Test
	@DisplayName("removeRefreshToken: refresh token을 삭제한다")
	void removeRefreshToken() {
		Long userId = 1L;
		String deviceId = "device-1";
		String refreshToken = "test-refresh-token";
		redisTemplate.opsForValue().set("user:" + userId + ":device:" + deviceId + ":refresh-token", refreshToken);

		repository.removeRefreshToken(userId, deviceId);

		String result = redisTemplate.opsForValue().get("user:" + userId + ":device:" + deviceId + ":refresh-token");
		assertThat(result).isNull();
	}
}
