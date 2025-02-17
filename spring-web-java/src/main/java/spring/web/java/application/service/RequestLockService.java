package spring.web.java.application.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import spring.web.java.domain.repository.RequestLockRepository;
import spring.web.java.presentation.exception.BaseException;
import spring.web.java.presentation.exception.ResponseMessage;

@Service
@RequiredArgsConstructor
public class RequestLockService {

	private final RequestLockRepository requestLockRepository;

	public void validate(Long memberId, String method, String uri) {
		if (!requestLockRepository.setIfAbsent(memberId, method, uri)) {
			throw new BaseException(ResponseMessage.DUPLICATE_REQUEST, HttpStatus.TOO_MANY_REQUESTS);
		}
	}
}
