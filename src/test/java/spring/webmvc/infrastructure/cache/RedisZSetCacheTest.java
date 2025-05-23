package spring.webmvc.infrastructure.cache;

import java.time.Duration;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import spring.webmvc.infrastructure.config.RedisTestContainerConfig;

@DataRedisTest
@Import(RedisTestContainerConfig.class)
class RedisZSetCacheTest {

	private final RedisZSetCache redisZSetCache;
	private final RedisTemplate<String, String> redisTemplate;

	@Autowired
	public RedisZSetCacheTest(RedisTemplate<String, String> redisTemplate) {
		this.redisZSetCache = new RedisZSetCache(redisTemplate, new ObjectMapper());
		this.redisTemplate = redisTemplate;
	}

	@BeforeEach
	void beforeEach() {
		redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
	}

	@Test
	@DisplayName("add: key, value, score 저장한다")
	void addCase1() {
		// Given
		String key = "testKey";
		String value = "testValue";
		double score = 1.0;

		// When
		redisZSetCache.add(key, value, score);

		// Then
		Set<String> result = redisTemplate.opsForZSet().range(key, 0, -1);
		Assertions.assertThat(result).contains(value);
	}

	@Test
	@DisplayName("add: key, value, score, duration 저장한다")
	void addCase2() throws InterruptedException {
		// Given
		String key = "testKey";
		String value = "testValue";
		double score = 1.0;
		Duration duration = Duration.ofMillis(1);

		// When
		redisZSetCache.add(key, value, score, duration);

		// Then
		Assertions.assertThat(redisTemplate.opsForZSet().range(key, 0, -1)).contains(value);
		Thread.sleep(duration);
		Assertions.assertThat(redisTemplate.opsForZSet().range(key, 0, -1)).isEmpty();
	}

	@Test
	@DisplayName("range: 범위 조회 후 반환한다")
	void range() {
		// Given
		String key = "testKey";
		String value1 = "testValue1";
		String value2 = "testValue2";
		String value3 = "testValue3";

		redisTemplate.opsForZSet().add(key, value1, 1.0);
		redisTemplate.opsForZSet().add(key, value2, 2.0);
		redisTemplate.opsForZSet().add(key, value3, 3.0);

		// When
		Set<String> result = redisZSetCache.range(key, 0, 1, String.class);

		// Then
		Assertions.assertThat(result).hasSize(2);
		Assertions.assertThat(result).contains(value1, value2);
	}
}