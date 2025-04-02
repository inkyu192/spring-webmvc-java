package spring.webmvc.infrastructure.persistence.adapter;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.repository.RequestLockRepository;
import spring.webmvc.infrastructure.persistence.RequestLockRedisRepository;

@Component
@RequiredArgsConstructor
public class RequestLockRepositoryAdapter implements RequestLockRepository {

	private final RequestLockRedisRepository redisRepository;

	@Override
	public boolean setIfAbsent(Long memberId, String method, String uri) {
		return redisRepository.setIfAbsent(memberId, method, uri);
	}
}
