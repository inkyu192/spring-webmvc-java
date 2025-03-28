package spring.web.kotlin.infrastructure.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import spring.web.kotlin.infrastructure.util.SecurityContextUtil
import java.util.*

@EnableJpaAuditing
@Configuration(proxyBeanMethods = false)
class JpaConfig {

    @Bean
    fun auditorProvider() = AuditorAware {
        runCatching { Optional.of(SecurityContextUtil.getMemberId()) }.getOrDefault(Optional.empty())
    }
}