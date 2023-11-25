package com.toy.shopwebmvc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toy.shopwebmvc.filter.JwtAuthenticationFilter;
import com.toy.shopwebmvc.filter.JwtAuthorizationFilter;
import com.toy.shopwebmvc.filter.JwtExceptionFilter;
import com.toy.shopwebmvc.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final ObjectMapper objectMapper;
    private final TokenService tokenService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @SneakyThrows
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .rememberMe(AbstractHttpConfigurer::disable)
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .logout(AbstractHttpConfigurer::disable)
                .formLogin(httpSecurityFormLoginConfigurer ->
                        httpSecurityFormLoginConfigurer
                                .usernameParameter("account")
                                .disable()
                )
                .addFilterBefore(new JwtExceptionFilter(objectMapper), JwtAuthenticationFilter.class)
                .addFilter(
                        new JwtAuthenticationFilter(
                                authenticationConfiguration.getAuthenticationManager(),
                                objectMapper,
                                tokenService
                        )
                )
                .addFilter(
                        new JwtAuthorizationFilter(
                                authenticationConfiguration.getAuthenticationManager(),
                                tokenService
                        )
                )
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .requestMatchers("/actuator/**").permitAll()
                                .requestMatchers("/login").permitAll()
                                .requestMatchers(HttpMethod.POST, "/member").permitAll()
                                .anyRequest().authenticated()
                )
                .build();
    }
}
