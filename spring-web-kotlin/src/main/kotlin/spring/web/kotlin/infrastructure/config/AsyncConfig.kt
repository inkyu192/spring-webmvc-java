package spring.web.kotlin.infrastructure.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@EnableAsync
@Configuration(proxyBeanMethods = false)
class AsyncConfig {

    @Bean
    fun threadPoolTaskExecutor() = ThreadPoolTaskExecutor()
        .apply {
            corePoolSize = 3
            maxPoolSize = 10
            queueCapacity = 100
            setThreadNamePrefix("Executor-")
        }
}