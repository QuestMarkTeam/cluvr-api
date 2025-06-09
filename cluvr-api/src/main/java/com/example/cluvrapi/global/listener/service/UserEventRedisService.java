package com.example.cluvrapi.global.listener.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.listener.enums.RedisKey;
import com.example.cluvrapi.global.response.ResponseCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class UserEventRedisService {
	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper objectMapper;

	public <T> void setZSetValue(String key, T dto, double score) {
		String json = toJson(dto);
		redisTemplate.opsForZSet().add(key, json, score);
	}

	public <T> String toJson(T dto) {
		try {
			return objectMapper.writeValueAsString(dto);
		} catch (JsonProcessingException e) {
			throw new BusinessException(ResponseCode.FAIL);
		}
	}

	public void incrementHashValue(String mainKey) {
		redisTemplate.opsForHash().increment(mainKey, RedisKey.TOTAL_ANSWER, 1);
		redisTemplate.opsForHash().increment(mainKey, RedisKey.TOTAL_SELECTED, 1);
		redisTemplate.opsForHash().increment(mainKey, RedisKey.TOTAL_SCORE, 1);
		redisTemplate.opsForHash().increment(mainKey, RedisKey.TOTAL_QUESTION, 1);
	}
}

