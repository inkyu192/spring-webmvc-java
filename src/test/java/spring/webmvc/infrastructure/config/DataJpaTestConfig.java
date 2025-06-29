package spring.webmvc.infrastructure.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import spring.webmvc.infrastructure.crypto.CryptoService;

@TestConfiguration
public class DataJpaTestConfig {

	@Bean
	public CryptoService cryptoUtil() {
		return Mockito.mock(CryptoService.class);
	}

	@Bean
	public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
		return new JPAQueryFactory(entityManager);
	}
}
