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

import spring.webmvc.infrastructure.config.RedisTestContainerConfig;

@DataRedisTest
@Import(RedisTestContainerConfig.class)
class RedisKeyValueCacheTest {

    private final RedisKeyValueCache redisKeyValueCache;
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public RedisKeyValueCacheTest(RedisTemplate<String, String> redisTemplate) {
        this.redisKeyValueCache = new RedisKeyValueCache(redisTemplate);
        this.redisTemplate = redisTemplate;
    }

    @BeforeEach
    void beforeEach() {
        redisTemplate.getConnectionFactory().getConnection().serverCommands();
    }

    @Test
    @DisplayName("setIfAbsent: value 있을 경우 false 반환한다")
    void setIfAbsentCase1() {
        // Given
        String key = "testKey";
        String value = "testValue";
        Duration duration = Duration.ofSeconds(1);

        redisKeyValueCache.setIfAbsent(key, value, duration);

        // When
        boolean result = redisKeyValueCache.setIfAbsent(key, value, duration);

        // Then
        Assertions.assertThat(result).isFalse();
    }

    @Test
    @DisplayName("setIfAbsent: value 없을 경우 true 반환한다")
    void setIfAbsentCase2() throws InterruptedException {
        // Given
        String key = "testKey";
        String value = "testValue";
        Duration duration = Duration.ofSeconds(1);

        redisKeyValueCache.setIfAbsent(key, value, duration);

        // When
        Thread.sleep(Duration.ofSeconds(1));
        boolean result = redisKeyValueCache.setIfAbsent(key, value, duration);

        // Then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    @DisplayName("get: 존재하는 key의 value를 반환한다")
    void getCase1() {
        // Given
        String key = "testKey";
        String value = "testValue";
        redisKeyValueCache.set(key, value);

        // When
        String result = redisKeyValueCache.get(key);

        // Then
        Assertions.assertThat(result).isEqualTo(value);
    }

    @Test
    @DisplayName("get: 존재하지 않는 key의 경우 null을 반환한다")
    void getCase2() {
        // Given
        String key = "nonExistentKey";

        // When
        String result = redisKeyValueCache.get(key);

        // Then
        Assertions.assertThat(result).isNull();
    }

    @Test
    @DisplayName("set: key-value를 저장한다")
    void setCase1() {
        // Given
        String key = "testKey";
        String value = "testValue";

        // When
        redisKeyValueCache.set(key, value);

        // Then
        String result = redisKeyValueCache.get(key);
        Assertions.assertThat(result).isEqualTo(value);
    }

    @Test
    @DisplayName("set: 기존 key가 있을 경우 value를 덮어쓴다")
    void setCase2() {
        // Given
        String key = "testKey";
        String value1 = "testValue1";
        String value2 = "testValue2";
        redisKeyValueCache.set(key, value1);

        // When
        redisKeyValueCache.set(key, value2);

        // Then
        String result = redisKeyValueCache.get(key);
        Assertions.assertThat(result).isEqualTo(value2);
    }

    @Test
    @DisplayName("set: timeout 설정과 함께 key-value를 저장한다")
    void setWithTimeoutCase1() {
        // Given
        String key = "testKey";
        String value = "testValue";
        Duration duration = Duration.ofSeconds(1);

        // When
        redisKeyValueCache.set(key, value, duration);

        // Then
        String result = redisKeyValueCache.get(key);
        Assertions.assertThat(result).isEqualTo(value);
    }

    @Test
    @DisplayName("set: timeout 이후에는 key-value가 만료된다")
    void setWithTimeoutCase2() throws InterruptedException {
        // Given
        String key = "testKey";
        String value = "testValue";
        Duration duration = Duration.ofSeconds(1);
        redisKeyValueCache.set(key, value, duration);

        // When
        Thread.sleep(Duration.ofSeconds(1));
        String result = redisKeyValueCache.get(key);

        // Then
        Assertions.assertThat(result).isNull();
    }

    @Test
    @DisplayName("delete: 존재하는 key를 삭제하고 true를 반환한다")
    void deleteCase1() {
        // Given
        String key = "testKey";
        String value = "testValue";
        redisKeyValueCache.set(key, value);

        // When
        boolean result = redisKeyValueCache.delete(key);

        // Then
        Assertions.assertThat(result).isTrue();
        Assertions.assertThat(redisKeyValueCache.get(key)).isNull();
    }

    @Test
    @DisplayName("delete: 존재하지 않는 key를 삭제하면 false를 반환한다")
    void deleteCase2() {
        // Given
        String key = "nonExistentKey";

        // When
        boolean result = redisKeyValueCache.delete(key);

        // Then
        Assertions.assertThat(result).isFalse();
    }

    @Test
    @DisplayName("increment: 숫자 값을 증가시키고 증가된 값을 반환한다")
    void incrementCase1() {
        // Given
        String key = "counterKey";
        long delta = 5;
        redisKeyValueCache.set(key, "10");

        // When
        Long result = redisKeyValueCache.increment(key, delta);

        // Then
        Assertions.assertThat(result).isEqualTo(15);
        Assertions.assertThat(redisKeyValueCache.get(key)).isEqualTo("15");
    }

    @Test
    @DisplayName("decrement: 숫자 값을 감소시키고 감소된 값을 반환한다")
    void decrementCase1() {
        // Given
        String key = "counterKey";
        long delta = 5;
        redisKeyValueCache.set(key, "10");

        // When
        Long result = redisKeyValueCache.decrement(key, delta);

        // Then
        Assertions.assertThat(result).isEqualTo(5);
        Assertions.assertThat(redisKeyValueCache.get(key)).isEqualTo("5");
    }

    @Test
    @DisplayName("decrement: 존재하지 않는 key의 경우 -delta 값으로 초기화한다")
    void decrementCase2() {
        // Given
        String key = "newCounterKey";
        long delta = 5;

        // When
        Long result = redisKeyValueCache.decrement(key, delta);

        // Then
        Assertions.assertThat(result).isEqualTo(-delta);
        Assertions.assertThat(redisKeyValueCache.get(key)).isEqualTo("-5");
    }
}
