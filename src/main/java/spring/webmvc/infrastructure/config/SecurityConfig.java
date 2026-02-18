package spring.webmvc.infrastructure.config;

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

import spring.webmvc.infrastructure.properties.AppProperties;
import spring.webmvc.infrastructure.security.JwtAuthenticationFilter;
import spring.webmvc.presentation.exception.handler.AccessDeniedExceptionHandler;
import spring.webmvc.presentation.exception.handler.AuthenticationExceptionHandler;
import spring.webmvc.presentation.exception.handler.JwtExceptionHandler;

@EnableMethodSecurity
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(
		HttpSecurity httpSecurity,
		AppProperties appProperties,
		AuthenticationExceptionHandler authenticationExceptionHandler,
		AccessDeniedExceptionHandler accessDeniedExceptionHandler,
		JwtAuthenticationFilter jwtAuthenticationFilter,
		JwtExceptionHandler jwtExceptionHandler
	) throws Exception {
		return httpSecurity
			.csrf(AbstractHttpConfigurer::disable)
			.anonymous(AbstractHttpConfigurer::disable)
			.rememberMe(AbstractHttpConfigurer::disable)
			.logout(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.exceptionHandling(exceptionHandling -> {
				exceptionHandling.authenticationEntryPoint(authenticationExceptionHandler);
				exceptionHandling.accessDeniedHandler(accessDeniedExceptionHandler);
			})
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.cors(cors -> cors.configurationSource(createCorsConfig(appProperties.cors())))
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(jwtExceptionHandler, JwtAuthenticationFilter.class)
			.build();
	}

	private CorsConfigurationSource createCorsConfig(AppProperties.CorsProperties corsProperties) {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(corsProperties.allowedOrigins());
		config.setAllowedOriginPatterns(corsProperties.allowedOriginPatterns());
		config.setAllowedMethods(corsProperties.allowedMethods());
		config.setAllowedHeaders(corsProperties.allowedHeaders());
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);

		return source;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
