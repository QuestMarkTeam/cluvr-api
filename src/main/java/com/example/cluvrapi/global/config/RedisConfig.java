package com.example.cluvrapi.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;


import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.config.SslProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
@RequiredArgsConstructor
public class RedisConfig {
	@Value("${REDIS_HOST:localhost}")
	private String redisHost;

	@Value("${REDIS_PORT:6379}")
	private int redisPort;

	@Bean(name = "redisStringTemplate")
	public RedisTemplate<String, String> redisStringTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();

		// redis template에 connection factory 연결
		redisTemplate.setConnectionFactory(redisConnectionFactory);

		// key에 대한 직렬화 방법 등록
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		// value에 대한 직렬화 방법 등록
		redisTemplate.setValueSerializer(new StringRedisSerializer());
		// hash key에 대한 직렬화 방법 등록
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		// hash value에 대한 직렬화 방법 등록
		redisTemplate.setHashValueSerializer(new StringRedisSerializer());

		return redisTemplate;
	}

	@Bean(name = "redisCountViewTemplate" )
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
	public RedissonClient redissonClient(
		@Value("${redis.host}") String redisHost,
		@Value("${redis.port}") int redisPort,
		@Value("${redis.ssl.enabled:false}") boolean sslEnabled
	) {
		Config config = new Config();
		SingleServerConfig serverConfig = config.useSingleServer();

		String scheme = sslEnabled ? "rediss" : "redis";
		serverConfig.setAddress(scheme + "://" + redisHost + ":" + redisPort)
			.setConnectionPoolSize(10)
			.setConnectionMinimumIdleSize(5)
			.setConnectTimeout(10000)
			.setTimeout(3000)
			.setRetryAttempts(3)
			.setRetryInterval(1500);

		if (sslEnabled) {
			serverConfig.setSslEnableEndpointIdentification(false); // 호스트 이름 검증 비활성
			serverConfig.setSslProvider(SslProvider.JDK); // 또는 OPENSSL 사용 가능
			// 필요 시: 인증서 파일 지정도 가능 (보통은 생략 가능)
			// serverConfig.setSslKeystore("classpath:your-truststore.jks");
			// serverConfig.setSslKeystorePassword("password");
		}

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
