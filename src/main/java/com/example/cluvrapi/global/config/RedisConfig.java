package com.example.cluvrapi.global.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
@EnableCaching
public class RedisConfig {

	@Value("${REDIS_HOST:localhost}")
	private String redisHost;

	@Value("${REDIS_PORT:6379}")
	private int redisPort;

	@Bean
	public RedisTemplate<String, Long> redisCountViewTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Long> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new GenericToStringSerializer<>(Long.class));
		return template;
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);

		// Redis용 ObjectMapper 설정
		ObjectMapper redisObjectMapper = createRedisObjectMapper();

		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer(redisObjectMapper));
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer(redisObjectMapper));
		template.setEnableTransactionSupport(true);
		template.afterPropertiesSet();

		return template;
	}
	@Bean
	public RedisTemplate<String, Long> longRedisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Long> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new GenericToStringSerializer<>(Long.class));
		return template;
	}

	@Bean(name = "redisCacheManager")
	public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
		// Redis용 ObjectMapper 설정
		ObjectMapper redisObjectMapper = createRedisObjectMapper();
		GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(redisObjectMapper);

		RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer))
			.disableCachingNullValues();

		// 각 캐시별 TTL 설정
		Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

		return RedisCacheManager.builder(connectionFactory)
			.cacheDefaults(defaultConfig.entryTtl(Duration.ofHours(1)))
			.withInitialCacheConfigurations(cacheConfigurations)
			.build();
	}

	// Redisson Client
	@Bean
	public RedissonClient redissonClient() {
		Config config = new Config();
		config.useSingleServer()
			.setAddress("redis://" + redisHost + ":" + redisPort)
			.setConnectionPoolSize(10)
			.setConnectionMinimumIdleSize(5)
			.setConnectTimeout(10000)
			.setTimeout(3000)
			.setRetryAttempts(3)
			.setRetryInterval(1500);

		return Redisson.create(config);
	}

	private ObjectMapper createRedisObjectMapper() {
		ObjectMapper redisObjectMapper = new ObjectMapper();
		redisObjectMapper.registerModule(new JavaTimeModule());

		redisObjectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		// 타입 검증기 설정
		PolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator.builder()
			.allowIfBaseType(Object.class)
			.build();

		redisObjectMapper.activateDefaultTyping(typeValidator, ObjectMapper.DefaultTyping.NON_FINAL_AND_ENUMS);

		return redisObjectMapper;
	}

}
