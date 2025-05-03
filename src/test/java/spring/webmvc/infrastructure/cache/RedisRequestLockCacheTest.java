package spring.webmvc.infrastructure.cache;

import java.time.Duration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

import spring.webmvc.infrastructure.config.RedisTestContainerConfig;

@DataRedisTest
@Import(RedisTestContainerConfig.class)
class RedisRequestLockCacheTest {

	private final RedisRequestLockCache requestLockCache;

	@Autowired
	public RedisRequestLockCacheTest(RedisTemplate<String, String> redisTemplate) {
		requestLockCache = new RedisRequestLockCache(redisTemplate);
	}

	@Test
	@DisplayName("setIfAbsent: RequestLock 있을 경우 false 반환한다")
	void setIfAbsentCase1() {
		// Given
		Long memberId = 1L;
		String method = "GET";
		String uri = "/members";

		requestLockCache.setIfAbsent(memberId, method, uri);

		// When
		boolean result = requestLockCache.setIfAbsent(memberId, method, uri);

		// Then
		Assertions.assertThat(result).isFalse();
	}

	@Test
	@DisplayName("setIfAbsent: RequestLock 없을 경우 저장 후 true 반환한다")
	void setIfAbsentCase2() throws InterruptedException {
		// Given
		Long memberId = 1L;
		String method = "GET";
		String uri = "/members";

		requestLockCache.setIfAbsent(memberId, method, uri);

		// When
		Thread.sleep(Duration.ofSeconds(1));
		boolean result = requestLockCache.setIfAbsent(memberId, method, uri);

		// Then
		Assertions.assertThat(result).isTrue();
	}
}