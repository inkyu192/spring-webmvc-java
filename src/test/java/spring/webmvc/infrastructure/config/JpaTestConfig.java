package spring.webmvc.infrastructure.config;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import spring.webmvc.infrastructure.crypto.CryptoService;

@TestConfiguration
@EnableJpaAuditing
public class JpaTestConfig {

	@Bean
	public CryptoService cryptoService() {
		CryptoService cryptoService = mock(CryptoService.class);
		when(cryptoService.encrypt(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
		when(cryptoService.decrypt(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
		return cryptoService;
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		return objectMapper;
	}

	@Bean
	public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
		return new JPAQueryFactory(entityManager);
	}
}
