package spring.webmvc.infrastructure.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.servlet.Filter;
import spring.webmvc.infrastructure.logging.HttpLogFilter;

@Configuration(proxyBeanMethods = false)
public class FilterConfig {

	@Bean
	public FilterRegistrationBean<Filter> filterRegistrationBean() {
		FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();

		bean.setFilter(new HttpLogFilter());
		bean.setOrder(Integer.MIN_VALUE);
		bean.addUrlPatterns("/*");

		return bean;
	}
}
