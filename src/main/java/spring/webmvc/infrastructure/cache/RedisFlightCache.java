package spring.webmvc.infrastructure.cache;

import java.time.Duration;
import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.webmvc.domain.cache.FlightCache;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisFlightCache implements FlightCache {

    private final RedisTemplate<String, String> redisTemplate;

    private String createKey(Long productId) {
        return "flight:%s".formatted(productId);
    }

    @Override
    public Optional<String> get(Long id) {
        try {
            String value = redisTemplate.opsForValue().get(createKey(id));
            return Optional.ofNullable(value);
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    @Override
    public void set(Long id, String value) {
        redisTemplate.opsForValue().set(createKey(id), value, Duration.ofHours(1));
    }
}