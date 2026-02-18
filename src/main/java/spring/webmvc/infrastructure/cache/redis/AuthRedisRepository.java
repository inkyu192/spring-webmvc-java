package spring.webmvc.infrastructure.cache.redis;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.webmvc.domain.model.vo.Email;
import spring.webmvc.domain.repository.cache.AuthCacheRepository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class AuthRedisRepository implements AuthCacheRepository {

	private static final String JOIN_VERIFY_KEY = "auth:join-verify:%s";
	private static final String PASSWORD_RESET_KEY = "auth:password-reset:%s";
	private static final Duration JOIN_VERIFY_TTL = Duration.ofMinutes(5);
	private static final Duration PASSWORD_RESET_TTL = Duration.ofMinutes(5);

	private final RedisTemplate<String, String> redisTemplate;

	@Override
	public void setJoinVerifyToken(String token, Email email) {
		String key = String.format(JOIN_VERIFY_KEY, token);

		try {
			redisTemplate.opsForValue().set(key, email.getValue(), JOIN_VERIFY_TTL);
		} catch (Exception e) {
			log.error("Failed to save join verify token email={}: {}", email.getValue(), e.getMessage(), e);
		}
	}

	@Override
	public String getJoinVerifyToken(String token) {
		String key = String.format(JOIN_VERIFY_KEY, token);

		try {
			return redisTemplate.opsForValue().get(key);
		} catch (Exception e) {
			log.warn("Failed to get join verify token token={}: {}", token, e.getMessage());
			return null;
		}
	}

	@Override
	public void deleteJoinVerifyToken(String token) {
		String key = String.format(JOIN_VERIFY_KEY, token);

		try {
			redisTemplate.delete(key);
		} catch (Exception e) {
			log.warn("Failed to delete join verify token token={}: {}", token, e.getMessage());
		}
	}

	@Override
	public void setPasswordResetToken(String token, Email email) {
		String key = String.format(PASSWORD_RESET_KEY, token);

		try {
			redisTemplate.opsForValue().set(key, email.getValue(), PASSWORD_RESET_TTL);
		} catch (Exception e) {
			log.error("Failed to save password reset token email={}: {}", email.getValue(), e.getMessage(), e);
		}
	}

	@Override
	public String getPasswordResetToken(String token) {
		String key = String.format(PASSWORD_RESET_KEY, token);

		try {
			return redisTemplate.opsForValue().get(key);
		} catch (Exception e) {
			log.warn("Failed to get password reset token token={}: {}", token, e.getMessage());
			return null;
		}
	}

	@Override
	public void deletePasswordResetToken(String token) {
		String key = String.format(PASSWORD_RESET_KEY, token);

		try {
			redisTemplate.delete(key);
		} catch (Exception e) {
			log.warn("Failed to delete password reset token token={}: {}", token, e.getMessage());
		}
	}
}
