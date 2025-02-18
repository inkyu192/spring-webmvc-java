package spring.web.java.infrastructure.config.security;

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.SneakyThrows;
import spring.web.java.presentation.exception.handler.ExceptionHandlingFilter;
import spring.web.java.presentation.exception.handler.AccessDeniedExceptionHandler;
import spring.web.java.presentation.exception.handler.AuthenticationExceptionHandler;

@EnableMethodSecurity
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

	@Bean
	@SneakyThrows
	public SecurityFilterChain securityFilterChain(
		HttpSecurity httpSecurity,
		CorsProperties corsProperties,
		AuthenticationExceptionHandler authenticationExceptionHandler,
		AccessDeniedExceptionHandler accessDeniedExceptionHandler,
		JwtAuthenticationFilter jwtAuthenticationFilter,
		ExceptionHandlingFilter exceptionHandlingFilter
	) {
		return httpSecurity
			.csrf(AbstractHttpConfigurer::disable)
			.anonymous(AbstractHttpConfigurer::disable)
			.rememberMe(AbstractHttpConfigurer::disable)
			.logout(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
				httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(authenticationExceptionHandler);
				httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(accessDeniedExceptionHandler);
			})
			.sessionManagement(httpSecuritySessionManagementConfigurer ->
				httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.cors(httpSecurityCorsConfigurer ->
				httpSecurityCorsConfigurer.configurationSource(generateCorsConfig(corsProperties)))
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(exceptionHandlingFilter, JwtAuthenticationFilter.class)
			.build();
	}

	private CorsConfigurationSource generateCorsConfig(CorsProperties corsProperties) {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(corsProperties.getAllowedOrigins());
		config.setAllowedMethods(corsProperties.getAllowedMethods());
		config.setAllowedHeaders(corsProperties.getAllowedHeaders());
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
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
}
