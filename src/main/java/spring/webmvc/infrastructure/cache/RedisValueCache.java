package spring.webmvc.infrastructure.cache;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.webmvc.domain.cache.ValueCache;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisValueCache implements ValueCache {

	private final RedisTemplate<String, String> redisTemplate;
	private final ObjectMapper objectMapper;

	@Override
	public String get(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	@Override
	public <T> T get(String key, Class<T> clazz) {
		String value = redisTemplate.opsForValue().get(key);
		return value != null ? deserialize(key, value, clazz) : null;
	}

	@Override
	public <T> void set(String key, T value) {
		set(key, value, null);
	}

	@Override
	public <T> void set(String key, T value, Duration timeout) {
		String stringValue = serialize(key, value);

		if (stringValue != null) {
			if (timeout == null) {
				redisTemplate.opsForValue().set(key, stringValue);
			} else {
				redisTemplate.opsForValue().set(key, stringValue, timeout);
			}
		}
	}

	@Override
	public boolean setIfAbsent(String key, String value, Duration timeout) {
		Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value, timeout);
		return Boolean.TRUE.equals(result);
	}

	@Override
	public boolean delete(String key) {
		return redisTemplate.delete(key);
	}

	@Override
	public Long increment(String key, long delta) {
		return redisTemplate.opsForValue().increment(key, delta);
	}

	@Override
	public Long decrement(String key, long delta) {
		return redisTemplate.opsForValue().decrement(key, delta);
	}

	private <T> String serialize(String key, T value) {
		if (value instanceof String stringValue) {
			return stringValue;
		}

		try {
			return objectMapper.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			log.warn("Failed to serialize value for key={}, value={}: {}", key, value, e.getMessage());
			return null;
		}
	}

	private <T> T deserialize(String key, String value, Class<T> clazz) {
		if (clazz == String.class) {
			return clazz.cast(value);
		}

		try {
			return objectMapper.readValue(value, clazz);
		} catch (JsonProcessingException e) {
			log.warn("Failed to deserialize value for key={}, value={}: {}", key, value, e.getMessage());
			return null;
		}
	}
}
