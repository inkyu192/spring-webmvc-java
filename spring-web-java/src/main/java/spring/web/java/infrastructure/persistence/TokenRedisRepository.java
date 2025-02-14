package spring.web.java.infrastructure.persistence;

import org.springframework.data.repository.CrudRepository;

import spring.web.java.domain.model.entity.Token;

public interface TokenRedisRepository extends CrudRepository<Token, Long> {
}
