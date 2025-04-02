package spring.webmvc.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.repository.RequestLockRepository;
import spring.webmvc.presentation.exception.DuplicateRequestException;

@Service
@RequiredArgsConstructor
public class RequestLockService {

	private final RequestLockRepository requestLockRepository;

	public void validate(Long memberId, String method, String uri) {
		if (!requestLockRepository.setIfAbsent(memberId, method, uri)) {
			throw new DuplicateRequestException(memberId, method, uri);
		}
	}
}
