package spring.webmvc.infrastructure.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import spring.webmvc.infrastructure.config.security.JwtProvider;
import spring.webmvc.infrastructure.support.ProblemDetailSupport;
import spring.webmvc.infrastructure.support.ResponseWriter;

@TestConfiguration
public class WebMvcTestConfig {

	@Bean
	public JwtProvider jwtProvider() {
		return Mockito.mock(JwtProvider.class);
	}

	@Bean
	public ResponseWriter responseWriter() {
		return Mockito.mock(ResponseWriter.class);
	}

	@Bean
	public ProblemDetailSupport problemDetailUtil() {
		return Mockito.mock(ProblemDetailSupport.class);
	}
}
