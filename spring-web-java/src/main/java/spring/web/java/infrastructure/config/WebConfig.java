package spring.web.java.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import spring.web.java.presentation.converter.DeliveryStatusConverter;
import spring.web.java.presentation.converter.OrderStatusConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new DeliveryStatusConverter());
		registry.addConverter(new OrderStatusConverter());
	}
}
