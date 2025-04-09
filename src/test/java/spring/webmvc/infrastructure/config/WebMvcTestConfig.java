package spring.webmvc.infrastructure.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import spring.webmvc.infrastructure.config.security.JwtTokenProvider;
import spring.webmvc.infrastructure.util.ResponseWriter;

@TestConfiguration
public class WebMvcTestConfig {

	@Bean
	public JwtTokenProvider jwtTokenProvider() {
		return Mockito.mock(JwtTokenProvider.class);
	}

	@Bean
	public ResponseWriter responseWriter() {
		return Mockito.mock(ResponseWriter.class);
	}
}
