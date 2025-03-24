package spring.web.java.infrastructure.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import spring.web.java.infrastructure.util.crypto.CryptoUtil;

@TestConfiguration
public class DataJpaTestConfig {

	@Bean
	public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
		return new JPAQueryFactory(entityManager);
	}

	@Bean
	public CryptoUtil cryptoUtil() {
		return Mockito.mock(CryptoUtil.class);
	}
}
