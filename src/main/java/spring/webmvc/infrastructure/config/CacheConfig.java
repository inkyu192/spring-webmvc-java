package spring.webmvc.infrastructure.config;

import java.time.Duration;
import java.util.Map;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@EnableCaching
@Configuration(proxyBeanMethods = false)
public class CacheConfig {

	@Bean
	public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.activateDefaultTyping(
			BasicPolymorphicTypeValidator.builder().allowIfBaseType(Object.class).build(),
			ObjectMapper.DefaultTyping.NON_FINAL,
			JsonTypeInfo.As.PROPERTY
		);

		GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

		RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
			.disableKeyPrefix()
			.entryTtl(Duration.ofHours(1))
			.serializeKeysWith(
				RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())
			)
			.serializeValuesWith(
				RedisSerializationContext.SerializationPair.fromSerializer(serializer)
			);

		Map<String, RedisCacheConfiguration> cacheConfigurations = Map.of(
			"productStock", defaultConfig.entryTtl(Duration.ofMinutes(10)),
			"curations", defaultConfig.entryTtl(Duration.ofDays(1)),
			"curationProducts", defaultConfig.entryTtl(Duration.ofHours(1))
		);

		return RedisCacheManager.builder(connectionFactory)
			.cacheDefaults(defaultConfig)
			.withInitialCacheConfigurations(cacheConfigurations)
			.build();
	}
}
