package spring.web.kotlin.global.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@Configuration(proxyBeanMethods = false)
class JpaConfig