package spring.web.kotlin.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@EnableAsync
@Configuration(proxyBeanMethods = false)
class AsyncConfig {

    @Bean
    fun threadPoolTaskExecutor() = ThreadPoolTaskExecutor()
        .also {
            it.corePoolSize = 3
            it.maxPoolSize = 10
            it.queueCapacity = 100
            it.setThreadNamePrefix("Executor-")
        }
}