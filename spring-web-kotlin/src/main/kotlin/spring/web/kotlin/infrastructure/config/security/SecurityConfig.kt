package spring.web.kotlin.infrastructure.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import spring.web.kotlin.presentation.exception.handler.AccessDeniedExceptionHandler
import spring.web.kotlin.presentation.exception.handler.AuthenticationExceptionHandler
import spring.web.kotlin.presentation.exception.handler.ExceptionHandlerFilter

@EnableMethodSecurity
@Configuration(proxyBeanMethods = false)
class SecurityConfig {

    @Bean
    fun securityFilterChain(
        httpSecurity: HttpSecurity,
        corsProperties: CorsProperties,
        authenticationExceptionHandler: AuthenticationExceptionHandler,
        accessDeniedExceptionHandler: AccessDeniedExceptionHandler,
        jwtAuthenticationFilter: JwtAuthenticationFilter,
        exceptionHandlerFilter: ExceptionHandlerFilter
    ): SecurityFilterChain = httpSecurity
        .csrf { it.disable() }
        .anonymous { it.disable() }
        .rememberMe { it.disable() }
        .logout { it.disable() }
        .httpBasic { it.disable() }
        .formLogin { it.disable() }
        .exceptionHandling {
            it.authenticationEntryPoint(authenticationExceptionHandler)
            it.accessDeniedHandler(accessDeniedExceptionHandler)
        }
        .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        .cors { it.configurationSource(createCorsConfig(corsProperties)) }
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        .addFilterBefore(exceptionHandlerFilter, jwtAuthenticationFilter.javaClass)
        .build()

    private fun createCorsConfig(corsProperties: CorsProperties): CorsConfigurationSource {
        val config = CorsConfiguration().apply {
            allowedOrigins = corsProperties.allowedOrigins
            allowedMethods = corsProperties.allowedMethods
            allowedHeaders = corsProperties.allowedHeaders
            allowCredentials = true
        }

        return UrlBasedCorsConfigurationSource().apply { registerCorsConfiguration("/**", config) }
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}