package spring.web.java.infrastructure.persistence;

import java.time.Duration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.data.redis.core.RedisTemplate;

@DataRedisTest
class RequestLockRedisRepositoryTest {

	private final RequestLockRedisRepository requestLockRedisRepository;

	@Autowired
	public RequestLockRedisRepositoryTest(RedisTemplate<String, String> redisTemplate) {
		this.requestLockRedisRepository = new RequestLockRedisRepository(redisTemplate);
	}

	@Test
	@DisplayName("setIfAbsent 는 기존 데이터가 있을 경우 저장하지 않는다")
	void case1() {
		// Given
		Long memberId = 1L;
		String method = "GET";
		String uri = "/members";

		requestLockRedisRepository.setIfAbsent(memberId, method, uri);

		// When
		boolean result = requestLockRedisRepository.setIfAbsent(memberId, method, uri);

		// Then
		Assertions.assertThat(result).isFalse();
	}

	@Test
	@DisplayName("setIfAbsent 는 기존 데이터가 없을 경우 저장한다")
	void case2() throws InterruptedException {
		// Given
		Long memberId = 1L;
		String method = "GET";
		String uri = "/members";

		requestLockRedisRepository.setIfAbsent(memberId, method, uri);

		// When
		Thread.sleep(Duration.ofSeconds(1));
		boolean result = requestLockRedisRepository.setIfAbsent(memberId, method, uri);

		// Then
		Assertions.assertThat(result).isTrue();
	}
}
