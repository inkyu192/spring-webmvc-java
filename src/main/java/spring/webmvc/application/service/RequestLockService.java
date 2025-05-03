package spring.webmvc.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.cache.RequestLockCache;
import spring.webmvc.presentation.exception.DuplicateRequestException;

@Service
@RequiredArgsConstructor
public class RequestLockService {

	private final RequestLockCache requestLockCache;

	public void validate(Long memberId, String method, String uri) {
		if (!requestLockCache.setIfAbsent(memberId, method, uri)) {
			throw new DuplicateRequestException(memberId, method, uri);
		}
	}
}
