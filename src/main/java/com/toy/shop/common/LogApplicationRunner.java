package com.toy.shop.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogApplicationRunner implements ApplicationRunner {

    private final ApplicationContext applicationContext;

    @Override
    public void run(ApplicationArguments args) {
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();

        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = applicationContext.getBean(beanDefinitionName);
            log.info("[{}] - [{}]", beanDefinitionName, bean);
        }
    }
}
