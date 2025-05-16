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
class RedisKeyValueCacheTest {

    private final RedisKeyValueCache keyValueCache;

    @Autowired
    public RedisKeyValueCacheTest(RedisTemplate<String, String> redisTemplate) {
        keyValueCache = new RedisKeyValueCache(redisTemplate);
    }

    @Test
    @DisplayName("setIfAbsent: value 있을 경우 false 반환한다")
    void setIfAbsentCase1() {
        // Given
        String key = "testKey";
        String value = "testValue";
        Duration duration = Duration.ofSeconds(1);

        keyValueCache.setIfAbsent(key, value, duration);

        // When
        boolean result = keyValueCache.setIfAbsent(key, value, duration);

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

        keyValueCache.setIfAbsent(key, value, duration);

        // When
        Thread.sleep(Duration.ofSeconds(1));
        boolean result = keyValueCache.setIfAbsent(key, value, duration);

        // Then
        Assertions.assertThat(result).isTrue();
    }
}