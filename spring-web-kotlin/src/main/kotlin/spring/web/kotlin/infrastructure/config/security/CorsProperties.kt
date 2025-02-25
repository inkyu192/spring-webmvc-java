package spring.web.kotlin.infrastructure.config.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "cors")
class CorsProperties {
    var allowedOrigins: List<String> = emptyList()
    var allowedMethods: List<String> = emptyList()
    var allowedHeaders: List<String> = emptyList()
}