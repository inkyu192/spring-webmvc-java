package spring.web.java.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import spring.web.java.infrastructure.security.JwtTokenProvider;
import spring.web.java.infrastructure.security.JwtAuthenticationFilter;
import spring.web.java.presentation.exception.ExceptionHandlerFilter;

@EnableMethodSecurity
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

	@Bean
	@SneakyThrows
	public SecurityFilterChain securityFilterChain(
		HttpSecurity httpSecurity,
		JwtAuthenticationFilter jwtAuthenticationFilter,
		ExceptionHandlerFilter exceptionHandlerFilter
	) {
		return httpSecurity
			.csrf(AbstractHttpConfigurer::disable)
			.anonymous(AbstractHttpConfigurer::disable)
			.rememberMe(AbstractHttpConfigurer::disable)
			.sessionManagement(httpSecuritySessionManagementConfigurer ->
				httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.logout(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(exceptionHandlerFilter, JwtAuthenticationFilter.class)
			.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public JwtTokenProvider jwtTokenProvider(
		@Value("${jwt.access-token.key}") String accessTokenKey,
		@Value("${jwt.access-token.expiration-time}") long accessTokenExpirationTime,
		@Value("${jwt.refresh-token.key}") String refreshTokenKey,
		@Value("${jwt.refresh-token.expiration-time}") long refreshTokenExpirationTime
	) {
		return new JwtTokenProvider(
			accessTokenKey,
			accessTokenExpirationTime,
			refreshTokenKey,
			refreshTokenExpirationTime
		);
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
		return new JwtAuthenticationFilter(jwtTokenProvider);
	}

	@Bean
	public ExceptionHandlerFilter jwtExceptionFilter(ObjectMapper objectMapper) {
		return new ExceptionHandlerFilter(objectMapper);
	}
}
