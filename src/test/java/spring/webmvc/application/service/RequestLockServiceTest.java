package spring.webmvc.application.service;

import java.time.Duration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import spring.webmvc.domain.cache.KeyValueCache;
import spring.webmvc.infrastructure.cache.CacheKey;
import spring.webmvc.presentation.exception.DuplicateRequestException;

@ExtendWith(MockitoExtension.class)
class RequestLockServiceTest {

	@InjectMocks
	private RequestLockService requestLockService;

	@Mock
	private KeyValueCache keyValueCache;

	@Test
	@DisplayName("validate: RequestLock 없을 경우 저장한다")
	void validateCase1() {
		// Given
		Long memberId = 1L;
		String method = "GET";
		String uri = "/members";

		String key = CacheKey.REQUEST_LOCK.generate(memberId, method, uri);
		String value = "1";
		Duration timeout = CacheKey.REQUEST_LOCK.getTimeout();

		Mockito.when(keyValueCache.setIfAbsent(key, value, timeout)).thenReturn(true);

		// When
		requestLockService.validate(memberId, method, uri);

		// Then
		Mockito.verify(keyValueCache, Mockito.times(1)).setIfAbsent(key, value, timeout);
	}

	@Test
	@DisplayName("validate: RequestLock 있을 경우 DuplicateRequestException 발생한다")
	void validateCase2() {
		// Given
		Long memberId = 1L;
		String method = "GET";
		String uri = "/members";

		String key = CacheKey.REQUEST_LOCK.generate(memberId, method, uri);
		String value = "1";
		Duration timeout = CacheKey.REQUEST_LOCK.getTimeout();

		Mockito.when(keyValueCache.setIfAbsent(key, value, timeout)).thenReturn(false);

		// When & Then
		Assertions.assertThatThrownBy(() -> requestLockService.validate(memberId, method, uri))
			.isInstanceOf(DuplicateRequestException.class);
	}
}