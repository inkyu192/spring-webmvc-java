package spring.webmvc.infrastructure.config;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import spring.webmvc.application.strategy.ProductStrategy;
import spring.webmvc.domain.model.enums.Category;

@Configuration(proxyBeanMethods = false)
public class AppConfig {

	@Bean
	public Map<Category, ProductStrategy> productStrategyMap(List<ProductStrategy> productStrategies) {
		return productStrategies.stream().collect(Collectors.toMap(ProductStrategy::getCategory, Function.identity()));
	}
}
