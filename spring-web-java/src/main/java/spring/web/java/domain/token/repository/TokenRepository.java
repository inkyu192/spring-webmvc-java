package spring.web.java.domain.token.repository;

import org.springframework.data.repository.CrudRepository;

import spring.web.java.domain.token.Token;

public interface TokenRepository extends CrudRepository<Token, Long> {
}
