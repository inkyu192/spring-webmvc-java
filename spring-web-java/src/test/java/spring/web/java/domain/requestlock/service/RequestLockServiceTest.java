package spring.web.java.domain.requestlock.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import spring.web.java.domain.requestlock.repository.RequestLockRepository;
import spring.web.java.global.exception.DomainException;

@ExtendWith(MockitoExtension.class)
class RequestLockServiceTest {

	@InjectMocks
	private RequestLockService requestLockService;

	@Mock
	private RequestLockRepository requestLockRepository;

	@Test
	void validate_notExist_shouldNotThrowException() {
		// Given
		Long memberId = 1L;
		String method = "GET";
		String uri = "/members";

		Mockito.when(requestLockRepository.setIfAbsent(memberId, method, uri)).thenReturn(true);

		// When
		requestLockService.validate(memberId, method, uri);

		// Then
		Mockito.verify(requestLockRepository, Mockito.times(1))
			.setIfAbsent(memberId, method, uri);
	}

	@Test
	void validate_exist_shouldThrowDomainException() {
		// Given
		Long memberId = 1L;
		String method = "GET";
		String uri = "/members";

		Mockito.when(requestLockRepository.setIfAbsent(memberId, method, uri)).thenReturn(false);

		// When & Then
		Assertions.assertThatThrownBy(() -> requestLockService.validate(memberId, method, uri))
			.isInstanceOf(DomainException.class);

		Mockito.verify(requestLockRepository, Mockito.times(1))
			.setIfAbsent(memberId, method, uri);
	}
}