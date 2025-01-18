package spring.web.java.domain.requestlock.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import spring.web.java.domain.requestlock.repository.RequestLockRepository;
import spring.web.java.global.common.ResponseMessage;
import spring.web.java.global.exception.DomainException;

@Service
@RequiredArgsConstructor
public class RequestLockService {

	private final RequestLockRepository requestLockRepository;

	public void validate(Long memberId, String method, String uri) {
		if (!requestLockRepository.setIfAbsent(memberId, method, uri)) {
			throw new DomainException(ResponseMessage.DUPLICATE_REQUEST, HttpStatus.TOO_MANY_REQUESTS);
		}
	}
}
