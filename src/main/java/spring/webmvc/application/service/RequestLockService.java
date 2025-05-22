package spring.webmvc.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.cache.ValueCache;
import spring.webmvc.domain.cache.CacheKey;
import spring.webmvc.presentation.exception.DuplicateRequestException;

@Service
@RequiredArgsConstructor
public class RequestLockService {

	private final ValueCache valueCache;

	public void validate(Long memberId, String method, String uri) {
		String key = CacheKey.REQUEST_LOCK.generate(memberId, method, uri);

		if (!valueCache.setIfAbsent(key, "1", CacheKey.REQUEST_LOCK.getTimeout())) {
			throw new DuplicateRequestException(memberId, method, uri);
		}
	}
}
