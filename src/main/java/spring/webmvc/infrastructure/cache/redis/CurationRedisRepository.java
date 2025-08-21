package spring.webmvc.infrastructure.cache.redis;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.cache.CurationCache;
import spring.webmvc.domain.model.cache.CurationProductCache;
import spring.webmvc.domain.repository.CurationCacheRepository;

@Repository
@RequiredArgsConstructor
public class CurationRedisRepository implements CurationCacheRepository {

	private final RedisTemplate<String, String> redisTemplate;
	private final ObjectMapper objectMapper;

	private static final String CURATIONS_KEY = "curations";

	@Override
	public void setCurations(List<CurationCache> curations) {
		try {
			String jsonValue = objectMapper.writeValueAsString(curations);
			redisTemplate.opsForValue().set(CURATIONS_KEY, jsonValue, Duration.ofHours(1));
		} catch (JsonProcessingException ignored) {
		}
	}

	@Override
	public List<CurationCache> getCurations() {
		String jsonValue = redisTemplate.opsForValue().get(CURATIONS_KEY);

		if (jsonValue != null) {
			try {
				return objectMapper.readValue(
					jsonValue,
					objectMapper.getTypeFactory().constructCollectionType(
						List.class,
						CurationProductCache.class
					)
				);
			} catch (Exception ignored) {
			}
		}

		return List.of();
	}

	@Override
	public void setCurationProducts(Long curationId, Long cursorId, Integer size, CurationProductCache cache) {
		String key = CURATIONS_KEY + ":" + curationId + ":cursor:" +
			(cursorId != null ? cursorId : "null") + ":size:" + size;

		try {
			String jsonValue = objectMapper.writeValueAsString(cache);
			redisTemplate.opsForValue().set(key, jsonValue, Duration.ofHours(1));
		} catch (Exception ignored) {
		}
	}

	@Override
	public CurationProductCache getCurationProducts(Long curationId, Long cursorId, Integer size) {
		String key = CURATIONS_KEY + ":" + curationId + ":cursor:" +
			(cursorId != null ? cursorId : "null") + ":size:" + size;
		String jsonValue = redisTemplate.opsForValue().get(key);

		if (jsonValue != null) {
			try {
				return objectMapper.readValue(jsonValue, CurationProductCache.class);
			} catch (Exception ignored) {
			}
		}

		return null;
	}

	@Override
	public void deleteAll() {
		Set<String> keys = redisTemplate.keys(CURATIONS_KEY + "*");

		redisTemplate.delete(keys);
	}
}
