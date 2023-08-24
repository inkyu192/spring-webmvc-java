package com.toy.shop.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toy.shop.business.token.service.TokenService;
import com.toy.shop.filter.JwtAuthenticationFilter;
import com.toy.shop.filter.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final TokenService tokenService;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @SneakyThrows
    public AuthenticationManager authenticationManager() {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().usernameParameter("account").disable()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), objectMapper, tokenService))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), tokenService))
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/members").permitAll()
                .requestMatchers("/members/**").hasRole("MEMBER")
                .requestMatchers(HttpMethod.GET, "/members").authenticated()
                .anyRequest().permitAll()
                .and()
                .build();
    }
}
