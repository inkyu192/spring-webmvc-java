package spring.webmvc.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.cache.KeyValueCache;
import spring.webmvc.domain.cache.CacheKey;
import spring.webmvc.presentation.exception.DuplicateRequestException;

@Service
@RequiredArgsConstructor
public class RequestLockService {

	private final KeyValueCache keyValueCache;

	public void validate(Long memberId, String method, String uri) {
		String key = CacheKey.REQUEST_LOCK.generate(memberId, method, uri);

		if (!keyValueCache.setIfAbsent(key, "1", CacheKey.REQUEST_LOCK.getTimeout())) {
			throw new DuplicateRequestException(memberId, method, uri);
		}
	}
}
