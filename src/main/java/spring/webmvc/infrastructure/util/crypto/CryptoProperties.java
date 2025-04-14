package spring.webmvc.infrastructure.util.crypto;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "crypto")
public class CryptoProperties {

	private final String secretKey;
	private final String ivParameter;
}
