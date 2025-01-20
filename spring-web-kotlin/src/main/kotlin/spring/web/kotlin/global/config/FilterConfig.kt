package spring.web.kotlin.global.config

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import spring.web.kotlin.global.filter.HttpLogFilter

@Configuration(proxyBeanMethods = false)
class FilterConfig {

    @Bean
    fun filterRegistrationBean() = FilterRegistrationBean(HttpLogFilter())
        .apply {
            order = Int.MIN_VALUE
            addUrlPatterns("/*")
        }
}