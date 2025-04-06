package spring.webmvc.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.repository.TokenRepository;
import spring.webmvc.infrastructure.persistence.TokenRedisRepository;

@Component
@RequiredArgsConstructor
public class TokenRepositoryAdapter implements TokenRepository {

	private final TokenRedisRepository redisRepository;

	@Override
	public Optional<String> findByMemberId(Long memberId) {
		return redisRepository.findByMemberId(memberId);
	}

	@Override
	public String save(Long memberId, String token) {
		return redisRepository.save(memberId, token);
	}
}
