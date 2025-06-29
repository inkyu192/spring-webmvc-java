package spring.webmvc.infrastructure.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import spring.webmvc.infrastructure.security.SecurityContextUtil;

@EnableJpaAuditing
@Configuration(proxyBeanMethods = false)
public class JpaConfig {

	@Bean
	public AuditorAware<Long> auditorProvider() {
		return () -> Optional.ofNullable(SecurityContextUtil.getMemberIdOrNull());
	}

	@Bean
	public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
		return new JPAQueryFactory(entityManager);
	}
}
