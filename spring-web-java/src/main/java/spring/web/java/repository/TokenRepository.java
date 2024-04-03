package spring.web.java.repository;


import org.springframework.data.repository.CrudRepository;
import spring.web.java.domain.Token;

public interface TokenRepository extends CrudRepository<Token, Long> {
}
