package com.example.cluvrapi.domain.clover.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.cluvrapi.global.event.service.EventRedisService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CloverEventRedisService extends EventRedisService {

	public CloverEventRedisService(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
		super(redisTemplate, objectMapper);
	}
}
