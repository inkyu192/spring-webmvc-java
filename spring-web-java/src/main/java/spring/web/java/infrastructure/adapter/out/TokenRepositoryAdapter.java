package spring.web.java.infrastructure.adapter.out;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.web.java.application.port.out.TokenRedisRepositoryPort;
import spring.web.java.application.port.out.TokenRepositoryPort;
import spring.web.java.domain.Token;

@Component
@RequiredArgsConstructor
public class TokenRepositoryAdapter implements TokenRepositoryPort {

	private final TokenRedisRepositoryPort redisRepository;

	@Override
	public Optional<Token> findById(Long id) {
		return redisRepository.findById(id);
	}

	@Override
	public Token save(Token token) {
		return redisRepository.save(token);
	}
}
