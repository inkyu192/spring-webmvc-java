package spring.webmvc.application.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import spring.webmvc.domain.repository.RequestLockRepository;
import spring.webmvc.presentation.exception.DuplicateRequestException;

@ExtendWith(MockitoExtension.class)
class RequestLockServiceTest {

	@InjectMocks
	private RequestLockService requestLockService;

	@Mock
	private RequestLockRepository requestLockRepository;

	@Test
	@DisplayName("validate 는 데이터가 없을 경우 저장한다")
	void case1() {
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
	@DisplayName("validate 는 데이터가 있을 경우 DuplicateRequestException 던진다")
	void case2() {
		// Given
		Long memberId = 1L;
		String method = "GET";
		String uri = "/members";

		Mockito.when(requestLockRepository.setIfAbsent(memberId, method, uri)).thenReturn(false);

		// When & Then
		Assertions.assertThatThrownBy(() -> requestLockService.validate(memberId, method, uri))
			.isInstanceOf(DuplicateRequestException.class);
	}
}