package spring.webmvc.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.pattern.PathPatternParser;

@Configuration(proxyBeanMethods = false)
public class WebConfig {

	@Bean
	public PathPatternParser pathPatternParser() {
		return new PathPatternParser();
	}
}
