package spring.web.kotlin.global.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import spring.web.kotlin.global.filter.JwtAuthenticationFilter
import spring.web.kotlin.global.filter.ExceptionHandlerFilter

@EnableMethodSecurity
@Configuration(proxyBeanMethods = false)
class SecurityConfig {

    @Bean
    fun securityFilterChain(
        httpSecurity: HttpSecurity,
        jwtAuthenticationFilter: JwtAuthenticationFilter,
        exceptionHandlerFilter: ExceptionHandlerFilter
    ): SecurityFilterChain = httpSecurity
        .csrf { it.disable() }
        .anonymous { it.disable() }
        .rememberMe { it.disable() }
        .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        .logout { it.disable() }
        .httpBasic { it.disable() }
        .formLogin { it.disable() }
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        .addFilterBefore(exceptionHandlerFilter, jwtAuthenticationFilter.javaClass)
        .build()

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun jwtTokenProvider(
        @Value("\${jwt.access-token.key}") accessTokenKey: String,
        @Value("\${jwt.access-token.expiration-time}") accessTokenExpirationTime: Long,
        @Value("\${jwt.refresh-token.key}") refreshTokenKey: String,
        @Value("\${jwt.refresh-token.expiration-time}") refreshTokenExpirationTime: Long,
    ) = JwtTokenProvider(accessTokenKey, accessTokenExpirationTime, refreshTokenKey, refreshTokenExpirationTime)

    @Bean
    fun jwtAuthenticationFilter(jwtTokenProvider: JwtTokenProvider) = JwtAuthenticationFilter(jwtTokenProvider)

    @Bean
    fun jwtExceptionFilter(objectMapper: ObjectMapper) = ExceptionHandlerFilter(objectMapper)
}