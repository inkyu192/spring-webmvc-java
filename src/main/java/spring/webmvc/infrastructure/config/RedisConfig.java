package spring.webmvc.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import lombok.RequiredArgsConstructor;
import spring.webmvc.infrastructure.properties.RedisProperties;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class RedisConfig {

	private final RedisProperties redisProperties;

	@Bean
	public LettuceConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory(redisProperties.host(), redisProperties.port());
	}
}
