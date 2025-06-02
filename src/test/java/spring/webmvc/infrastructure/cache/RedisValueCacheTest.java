package spring.webmvc.infrastructure.cache;

import java.time.Duration;

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
class RedisValueCacheTest {

	private final RedisValueCache redisKeyValueCache;
	private final RedisTemplate<String, String> redisTemplate;

	@Autowired
	public RedisValueCacheTest(RedisTemplate<String, String> redisTemplate) {
		this.redisKeyValueCache = new RedisValueCache(redisTemplate, new ObjectMapper());
		this.redisTemplate = redisTemplate;
	}

	@BeforeEach
	void beforeEach() {
		redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
	}

	@Test
	@DisplayName("setIfAbsent: key-value 있을 경우 false 반환한다")
	void setIfAbsentCase1() {
		// Given
		String key = "testKey";
		String value = "testValue";
		Duration duration = Duration.ofMillis(100);

		redisTemplate.opsForValue().set(key, value);

		// When
		boolean result = redisKeyValueCache.setIfAbsent(key, value, duration);

		// Then
		Assertions.assertThat(result).isFalse();
	}

	@Test
	@DisplayName("setIfAbsent: key-value 없을 경우 true 반환한다")
	void setIfAbsentCase2() {
		// Given
		String key = "testKey";
		String value = "testValue";
		Duration duration = Duration.ofMillis(100);

		// When
		boolean result = redisKeyValueCache.setIfAbsent(key, value, duration);

		// Then
		Assertions.assertThat(result).isTrue();
	}

	@Test
	@DisplayName("get: key-value 있을 경우 value 반환한다")
	void getCase1() {
		// Given
		String key = "testKey";
		String value = "testValue";

		redisTemplate.opsForValue().set(key, value);

		// When
		String result = redisKeyValueCache.get(key);

		// Then
		Assertions.assertThat(result).isEqualTo(value);
	}

	@Test
	@DisplayName("get: key-value 없을 경우 null 반환한다")
	void getCase2() {
		// Given
		String key = "testKey";

		// When
		String result = redisKeyValueCache.get(key);

		// Then
		Assertions.assertThat(result).isNull();
	}

	@Test
	@DisplayName("set: key-value 저장한다")
	void setCase1() {
		// Given
		String key = "testKey";
		String value = "testValue";

		// When
		redisKeyValueCache.set(key, value);

		// Then
		String result = redisTemplate.opsForValue().get(key);
		Assertions.assertThat(result).isEqualTo(value);
	}

	@Test
	@DisplayName("set: duration 있을 경우 duration 동안 key-value 저장한다")
	void setCase2() throws InterruptedException {
		// Given
		String key = "testKey";
		String value = "testValue";
		Duration duration = Duration.ofMillis(100);

		// When
		redisKeyValueCache.set(key, value, duration);

		// Then
		Assertions.assertThat(redisTemplate.opsForValue().get(key)).isEqualTo(value);
		Thread.sleep(duration);
		Assertions.assertThat(redisTemplate.opsForValue().get(key)).isNull();
	}

	@Test
	@DisplayName("delete: key-value 있을 경우 삭제 후 true 반환한다")
	void deleteCase1() {
		// Given
		String key = "testKey";
		String value = "testValue";

		redisTemplate.opsForValue().set(key, value);

		// When
		boolean result = redisKeyValueCache.delete(key);

		// Then
		Assertions.assertThat(result).isTrue();
		Assertions.assertThat(redisTemplate.opsForValue().get(key)).isNull();
	}

	@Test
	@DisplayName("delete: key-value 없을 경우 false 반환한다")
	void deleteCase2() {
		// Given
		String key = "testKey";

		// When
		boolean result = redisKeyValueCache.delete(key);

		// Then
		Assertions.assertThat(result).isFalse();
	}

	@Test
	@DisplayName("increment: 숫자 값을 증가시키고 증가된 값을 반환한다")
	void increment() {
		// Given
		String key = "testKey";
		long delta = 5;

		redisTemplate.opsForValue().set(key, "10");

		// When
		Long result = redisKeyValueCache.increment(key, delta);

		// Then
		Assertions.assertThat(result).isEqualTo(15);
		Assertions.assertThat(redisTemplate.opsForValue().get(key)).isEqualTo("15");
	}

	@Test
	@DisplayName("decrement: 숫자 값을 감소시키고 감소된 값을 반환한다")
	void decrement() {
		// Given
		String key = "testKey";
		long delta = 5;

		redisTemplate.opsForValue().set(key, "10");

		// When
		Long result = redisKeyValueCache.decrement(key, delta);

		// Then
		Assertions.assertThat(result).isEqualTo(5);
		Assertions.assertThat(redisTemplate.opsForValue().get(key)).isEqualTo("5");
	}
}
