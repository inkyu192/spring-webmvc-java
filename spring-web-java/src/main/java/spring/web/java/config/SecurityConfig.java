package spring.web.java.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import spring.web.java.config.security.JwtAuthenticationFilter;
import spring.web.java.config.security.JwtExceptionFilter;
import spring.web.java.config.security.JwtTokenProvider;
import spring.web.java.config.security.UserDetailsServiceImpl;
import spring.web.java.repository.MemberRepository;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

    @Bean
    @SneakyThrows
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            JwtExceptionFilter jwtExceptionFilter
    ) {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .rememberMe(AbstractHttpConfigurer::disable)
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .logout(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class)
                .addFilter(jwtAuthenticationFilter)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .requestMatchers("/actuator/**").permitAll()
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/member").permitAll()
                                .anyRequest().authenticated()
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(MemberRepository memberRepository) {
        return new UserDetailsServiceImpl(memberRepository);
    }

    @Bean
    @SneakyThrows
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider(
            @Value("${jwt.access-token.key}") String accessTokenKey,
            @Value("${jwt.access-token.expiration-time}") long accessTokenExpirationTime,
            @Value("${jwt.refresh-token.key}") String refreshTokenKey,
            @Value("${jwt.refresh-token.expiration-time}") long refreshTokenExpirationTime
    ) {
        return new JwtTokenProvider(accessTokenKey, accessTokenExpirationTime, refreshTokenKey, refreshTokenExpirationTime);
    }

    @Bean
    @SneakyThrows
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider
    ) {
        return new JwtAuthenticationFilter(authenticationManager, jwtTokenProvider);
    }

    @Bean
    public JwtExceptionFilter jwtExceptionFilter(ObjectMapper objectMapper) {
        return new JwtExceptionFilter(objectMapper);
    }
}
