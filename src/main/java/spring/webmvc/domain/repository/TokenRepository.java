package spring.webmvc.domain.repository;

import java.util.Optional;

import spring.webmvc.domain.model.entity.Token;

public interface TokenRepository {

	Optional<Token> findById(Long id);

	Token save(Token token);
}
