package spring.webmvc.infrastructure.config.security;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {
	private final List<String> allowedOrigins;
	private final List<String> allowedMethods;
	private final List<String> allowedHeaders;
}
