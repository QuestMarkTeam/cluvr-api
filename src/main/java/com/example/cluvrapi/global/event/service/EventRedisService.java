package com.example.cluvrapi.global.event.service;

import java.time.Duration;

import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.ResponseCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Service
@RequiredArgsConstructor
public class EventRedisService {
	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper objectMapper;

	public <T> void setZSetValue(String key, T dto, double score) {
		String json = toJson(dto);
		redisTemplate.opsForZSet().add(key, json, score);
	}

	public <T> String toJson(T dto) {
		try {
			objectMapper.registerModule(new JavaTimeModule()); // 시간 타입을 직렬화하려면 필요
			objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			return objectMapper.writeValueAsString(dto);
		} catch (JsonProcessingException e) {
			throw new BusinessException(ResponseCode.FAIL);
		}
	}

	// redis 만료 설정
	public void setExpireOfDay(String key, Long days) {
		redisTemplate.expire(key, Duration.ofDays(days));
	}

}
