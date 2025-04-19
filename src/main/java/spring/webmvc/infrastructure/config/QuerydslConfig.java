package spring.webmvc.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Configuration(proxyBeanMethods = false)
public class QuerydslConfig {

	@Bean
	public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
		return new JPAQueryFactory(entityManager);
	}
}
