package spring.web.kotlin.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import spring.web.kotlin.config.security.JwtAuthenticationFilter
import spring.web.kotlin.config.security.JwtExceptionFilter
import spring.web.kotlin.config.security.JwtTokenProvider
import spring.web.kotlin.config.security.UserDetailsServiceImpl
import spring.web.kotlin.repository.MemberRepository

@Configuration(proxyBeanMethods = false)
class SecurityConfig {

    @Bean
    fun securityFilterChain(
        httpSecurity: HttpSecurity,
        jwtAuthenticationFilter: JwtAuthenticationFilter,
        jwtExceptionFilter: JwtExceptionFilter
    ): SecurityFilterChain = httpSecurity
        .csrf { it.disable() }
        .rememberMe { it.disable() }
        .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        .logout { it.disable() }
        .httpBasic { it.disable() }
        .formLogin { it.disable() }
        .addFilterBefore(jwtExceptionFilter, jwtAuthenticationFilter.javaClass)
        .addFilter(jwtAuthenticationFilter)
        .authorizeHttpRequests {
            it.requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/member/**").authenticated()
                .anyRequest().permitAll()
        }
        .build()

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun userDetailService(memberRepository: MemberRepository) = UserDetailsServiceImpl(memberRepository)

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager =
        authenticationConfiguration.authenticationManager

    @Bean
    fun jwtTokenProvider(
        @Value("\${jwt.access-token.key}") accessTokenKey: String,
        @Value("\${jwt.access-token.expiration-time}") accessTokenExpirationTime: Long,
        @Value("\${jwt.refresh-token.key}") refreshTokenKey: String,
        @Value("\${jwt.refresh-token.expiration-time}") refreshTokenExpirationTime: Long,
    ) = JwtTokenProvider(accessTokenKey, accessTokenExpirationTime, refreshTokenKey, refreshTokenExpirationTime)

    @Bean
    fun jwtAuthenticationFilter(authenticationManager: AuthenticationManager, jwtTokenProvider: JwtTokenProvider) =
        JwtAuthenticationFilter(authenticationManager, jwtTokenProvider)

    @Bean
    fun jwtExceptionFilter(objectMapper: ObjectMapper) = JwtExceptionFilter(objectMapper)
}