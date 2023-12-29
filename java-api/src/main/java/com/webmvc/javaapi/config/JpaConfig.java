package com.webmvc.javaapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@Configuration(proxyBeanMethods = false)
public class JpaConfig {
}
