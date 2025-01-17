package spring.web.java.domain.requestlock.repository;

import java.time.Duration;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;

import spring.web.java.domain.requestlock.RequestLock;

@DataRedisTest
class RequestLockRepositoryTest {

	@Autowired
	private RequestLockRepository requestLockRepository;

	@Test
	void notExpiration() {
		// Given
		RequestLock requestLock = RequestLock.create("1|POST|/members");
		requestLockRepository.save(requestLock);

		// When
		Optional<RequestLock> result = requestLockRepository.findById(requestLock.getRequestId());

		// Then
		Assertions.assertThat(result).isPresent();
	}

	@Test
	void expiration() throws InterruptedException {
		// Given
		RequestLock requestLock = RequestLock.create("1|POST|/members");
		requestLockRepository.save(requestLock);

		// When
		Thread.sleep(Duration.ofSeconds(1));
		Optional<RequestLock> result = requestLockRepository.findById(requestLock.getRequestId());

		// Then
		Assertions.assertThat(result).isNotPresent();
	}
}