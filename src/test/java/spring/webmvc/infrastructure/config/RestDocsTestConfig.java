package spring.webmvc.infrastructure.config;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class RestDocsTestConfig {

	@Bean
	public RestDocsMockMvcConfigurationCustomizer restDocsMockMvcConfigurationCustomizer() {
		return configurer -> configurer.operationPreprocessors()
			.withRequestDefaults(prettyPrint())
			.withResponseDefaults(prettyPrint());
	}
}
