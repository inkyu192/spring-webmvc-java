package spring.webmvc.infrastructure.cache;

import java.time.Duration;
import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.cache.TokenCache;

@Component
@RequiredArgsConstructor
public class RedisTokenCache implements TokenCache {

	private final RedisTemplate<String, String> redisTemplate;

	private String createKey(Long memberId) {
		return "member:%s:token:refresh".formatted(memberId);
	}

	@Override
	public Optional<String> get(Long memberId) {
		String value = redisTemplate.opsForValue().get(createKey(memberId));
		return Optional.ofNullable(value);
	}

	@Override
	public void set(Long memberId, String value) {
		redisTemplate.opsForValue().set(createKey(memberId), value, Duration.ofDays(7));
	}
}
