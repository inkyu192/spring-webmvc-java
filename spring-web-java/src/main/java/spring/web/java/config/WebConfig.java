package spring.web.java.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import spring.web.java.convertor.core.DeliveryStatusConverter;
import spring.web.java.convertor.core.OrderStatusConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new DeliveryStatusConverter());
		registry.addConverter(new OrderStatusConverter());
	}
}
