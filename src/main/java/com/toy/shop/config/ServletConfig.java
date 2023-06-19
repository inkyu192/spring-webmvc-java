package com.toy.shop.config;

import com.toy.shop.filter.FirstFilter;
import com.toy.shop.filter.SecondFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@code @Component} 방식은 {@code @Order}로 순서는 지정할 수 있으나 url 패턴 매핑이 불가
 * <p>{@code @ServletComponentScan} + {@code @WebFilter} 방식은 url 패턴 매핑은 가능하나 순서 지정 불가
 * <p>{@code FilterRegistrationBean} 방식은 url 패턴 매핑, 순서 지정 둘다 가능
 */
@Configuration(proxyBeanMethods = false)
//@ServletComponentScan(basePackages = "com.toy.shop.filter")
public class ServletConfig {

    @Bean
    public FilterRegistrationBean<Filter> firstFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new FirstFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> secondFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new SecondFilter());
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }
}
