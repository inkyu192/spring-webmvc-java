package spring.web.java.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import spring.web.java.domain.model.entity.Token;
import spring.web.java.domain.repository.TokenRepository;
import spring.web.java.infrastructure.persistence.TokenRedisRepository;

@Repository
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
