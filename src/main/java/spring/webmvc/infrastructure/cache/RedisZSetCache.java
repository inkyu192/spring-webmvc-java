package spring.webmvc.infrastructure.cache;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.webmvc.domain.cache.ZSetCache;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisZSetCache implements ZSetCache {

	private final RedisTemplate<String, String> redisTemplate;
	private final ObjectMapper objectMapper;

	@Override
	public <T> void add(String key, T value, double score) {
		String stringValue = serialize(key, value);

		if (stringValue != null) {
			redisTemplate.opsForZSet().add(key, stringValue, score);
		}
	}

	@Override
	public <T> void add(String key, T value, double score, Duration duration) {
		String stringValue = serialize(key, value);

		if (stringValue != null) {
			redisTemplate.opsForZSet().add(key, stringValue, score);
			redisTemplate.expire(key, duration);
		}
	}

	@Override
	public <T> Set<T> range(String key, long start, long end, Class<T> clazz) {
		Set<String> rawValues = redisTemplate.opsForZSet().range(key, start, end);

		if (rawValues == null) {
			return Collections.emptySet();
		}

		return rawValues.stream()
			.map(value -> deserialize(key, value, clazz))
			.filter(Objects::nonNull)
			.collect(Collectors.toCollection(LinkedHashSet::new));
	}

	private <T> String serialize(String key, T value) {
		if (value instanceof String stringValue) {
			return stringValue;
		}

		try {
			return objectMapper.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			log.warn("Failed to serialize zSet for key={}, value={}: {}", key, value, e.getMessage());
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
			log.warn("Failed to deserialize zSet for key={}, value={}: {}", key, value, e.getMessage());
			return null;
		}
	}
}
