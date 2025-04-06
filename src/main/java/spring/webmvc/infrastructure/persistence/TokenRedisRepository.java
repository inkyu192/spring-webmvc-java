package spring.webmvc.infrastructure.persistence;

import java.time.Duration;
import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TokenRedisRepository {

	private final RedisTemplate<String, String> redisTemplate;

	private String createKey(Long memberId) {
		return "member:%s:token:refresh".formatted(memberId);
	}

	public Optional<String> findByMemberId(Long memberId) {
		String key = createKey(memberId);
		return Optional.ofNullable(redisTemplate.opsForValue().get(key));
	}

	public String save(Long memberId, String token) {
		String key = createKey(memberId);

		redisTemplate.opsForValue().set(key, token, Duration.ofDays(7));

		return token;
	}
}
