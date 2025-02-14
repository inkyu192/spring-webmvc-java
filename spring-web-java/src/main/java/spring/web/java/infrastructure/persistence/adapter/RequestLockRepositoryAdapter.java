package spring.web.java.infrastructure.persistence.adapter;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import spring.web.java.domain.repository.RequestLockRepository;
import spring.web.java.infrastructure.persistence.RequestLockRedisRepository;

@Repository
@RequiredArgsConstructor
public class RequestLockRepositoryAdapter implements RequestLockRepository {

	private final RequestLockRedisRepository redisRepository;

	@Override
	public boolean setIfAbsent(Long memberId, String method, String uri) {
		return redisRepository.setIfAbsent(memberId, method, uri);
	}
}
