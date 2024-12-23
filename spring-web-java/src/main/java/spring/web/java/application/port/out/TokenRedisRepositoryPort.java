package spring.web.java.application.port.out;

import org.springframework.data.repository.CrudRepository;

import spring.web.java.domain.Token;

public interface TokenRedisRepositoryPort extends CrudRepository<Token, Long> {
}
