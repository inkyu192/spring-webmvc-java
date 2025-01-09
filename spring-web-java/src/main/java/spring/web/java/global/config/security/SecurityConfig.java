package spring.web.java.global.config.security;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import spring.web.java.domain.member.repository.MemberRepository;
import spring.web.java.global.common.JwtTokenProvider;
import spring.web.java.global.filter.JwtAuthenticationFilter;
import spring.web.java.global.filter.JwtExceptionFilter;

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
					.requestMatchers(HttpMethod.OPTIONS).permitAll()
					.requestMatchers("/actuator/**").permitAll()
					.requestMatchers("/token/**").permitAll()
					.requestMatchers("/member/login").permitAll()
					.requestMatchers(HttpMethod.GET, "/member/**").authenticated()
					.anyRequest().permitAll()
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
		return new JwtTokenProvider(accessTokenKey, accessTokenExpirationTime, refreshTokenKey,
			refreshTokenExpirationTime);
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
