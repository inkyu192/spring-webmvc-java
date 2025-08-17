package spring.webmvc.infrastructure.cache.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.repository.TokenCacheRepository;

@Component
@RequiredArgsConstructor
public class TokenCacheRedisRepository implements TokenCacheRepository {

	private final RedisTemplate<String, String> redisTemplate;

	@Override
	public void setRefreshToken(Long memberId, String refreshToken) {
		redisTemplate.opsForValue().set("member:%s:token:refresh".formatted(memberId), refreshToken);
	}

	@Override
	public String getRefreshToken(Long memberId) {
		return redisTemplate.opsForValue().get("member:%s:token:refresh".formatted(memberId));
	}
}
