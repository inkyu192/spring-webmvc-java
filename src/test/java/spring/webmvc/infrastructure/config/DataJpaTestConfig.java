package spring.webmvc.infrastructure.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import spring.webmvc.infrastructure.crypto.CryptoService;

@TestConfiguration
public class DataJpaTestConfig {

	@Bean
	public CryptoService cryptoUtil() {
		return Mockito.mock(CryptoService.class);
	}
}
