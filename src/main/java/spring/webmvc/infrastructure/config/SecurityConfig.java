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

import lombok.SneakyThrows;
import spring.webmvc.infrastructure.security.JwtAuthenticationFilter;
import spring.webmvc.infrastructure.properties.CorsProperties;
import spring.webmvc.presentation.exception.handler.AccessDeniedExceptionHandler;
import spring.webmvc.presentation.exception.handler.AuthenticationExceptionHandler;
import spring.webmvc.presentation.exception.handler.JwtExceptionHandler;

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
		JwtExceptionHandler jwtExceptionHandler
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
				httpSecurityCorsConfigurer.configurationSource(createCorsConfig(corsProperties)))
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(jwtExceptionHandler, JwtAuthenticationFilter.class)
			.build();
	}

	private CorsConfigurationSource createCorsConfig(CorsProperties corsProperties) {
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
}
