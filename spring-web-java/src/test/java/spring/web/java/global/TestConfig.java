package spring.web.java.global;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import spring.web.java.global.common.AESCrypto;

@TestConfiguration
public class TestConfig {

	@Bean
	public AESCrypto aesCrypto() {
		return new AESCrypto();
	}
}
