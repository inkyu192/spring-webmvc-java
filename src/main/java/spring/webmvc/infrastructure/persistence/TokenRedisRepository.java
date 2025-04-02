package spring.webmvc.infrastructure.persistence;

import org.springframework.data.repository.CrudRepository;

import spring.webmvc.domain.model.entity.Token;

public interface TokenRedisRepository extends CrudRepository<Token, Long> {
}
