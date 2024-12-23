package spring.web.java.application.port.out;

import java.util.Optional;

import spring.web.java.domain.Token;

public interface TokenRepositoryPort {

	Optional<Token> findById(Long id);

	Token save(Token token);
}
