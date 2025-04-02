package spring.webmvc.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Token;
import spring.webmvc.domain.repository.TokenRepository;
import spring.webmvc.infrastructure.persistence.TokenRedisRepository;

@Component
@RequiredArgsConstructor
public class TokenRepositoryAdapter implements TokenRepository {

	private final TokenRedisRepository redisRepository;

	@Override
	public Optional<Token> findById(Long id) {
		return redisRepository.findById(id);
	}

	@Override
	public Token save(Token token) {
		return redisRepository.save(token);
	}
}
