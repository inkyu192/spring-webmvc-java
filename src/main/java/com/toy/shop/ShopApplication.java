package com.toy.shop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Slf4j
@EnableJpaAuditing
@SpringBootApplication
public class ShopApplication {
	@Bean
	public ApplicationRunner applicationRunner(ApplicationContext applicationContext) {
		return args -> {
			String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();

			for (String beanDefinitionName : beanDefinitionNames) {
				Object bean = applicationContext.getBean(beanDefinitionName);
				log.info("name: {}, object: {}", beanDefinitionName, bean);
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(ShopApplication.class, args);
	}
}
