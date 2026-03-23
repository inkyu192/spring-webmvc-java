package spring.webmvc.infrastructure.config;

import java.util.List;
import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.util.pattern.PathPatternParser;

@Configuration(proxyBeanMethods = false)
public class WebConfig {

	@Bean
	public PathPatternParser pathPatternParser() {
		return new PathPatternParser();
	}

	@Bean
	public LocaleResolver localeResolver() {
		AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
		resolver.setDefaultLocale(Locale.KOREAN);
		resolver.setSupportedLocales(List.of(Locale.KOREAN, Locale.ENGLISH));
		return resolver;
	}
}
