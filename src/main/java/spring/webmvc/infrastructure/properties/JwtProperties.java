package spring.webmvc.infrastructure.properties;

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
	public static class TokenProperties {
		private final String key;
		private final Duration expiration;
	}
}
