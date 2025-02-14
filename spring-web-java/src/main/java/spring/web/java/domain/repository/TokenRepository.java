package spring.web.java.domain.repository;

import java.util.Optional;

import spring.web.java.domain.model.entity.Token;

public interface TokenRepository {

	Optional<Token> findById(Long id);

	Token save(Token token);
}
