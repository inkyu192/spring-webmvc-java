package spring.webmvc.infrastructure.config.security;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

	private final TokenProperties accessToken;
	private final TokenProperties refreshToken;

	@Getter
	@RequiredArgsConstructor
	static class TokenProperties {
		private final String key;
		private final Duration expiration;
	}
}
