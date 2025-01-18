package spring.web.java.domain.requestlock.repository;

import java.time.Duration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;

@DataRedisTest
class RequestLockRepositoryTest {

	private final RequestLockRepository requestLockRepository;

	@Autowired
	public RequestLockRepositoryTest(RedisTemplate<String, String> redisTemplate) {
		this.requestLockRepository = new RequestLockRepository(redisTemplate);
	}

	@Test
	void notExpiration() {
		// Given
		Long memberId = 1L;
		String method = "GET";
		String uri = "/members";

		requestLockRepository.setIfAbsent(memberId, method, uri);

		// When
		boolean result = requestLockRepository.setIfAbsent(memberId, method, uri);

		// Then
		Assertions.assertThat(result).isFalse();
	}

	@Test
	void expiration() throws InterruptedException {
		// Given
		Long memberId = 1L;
		String method = "GET";
		String uri = "/members";

		requestLockRepository.setIfAbsent(memberId, method, uri);

		// When
		Thread.sleep(Duration.ofSeconds(1));
		boolean result = requestLockRepository.setIfAbsent(memberId, method, uri);

		// Then
		Assertions.assertThat(result).isTrue();
	}
}
