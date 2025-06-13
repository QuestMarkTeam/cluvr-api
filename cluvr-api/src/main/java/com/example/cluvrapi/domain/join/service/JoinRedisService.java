package com.example.cluvrapi.domain.join.service;

import java.util.Map;

import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinRedisService {
	private final RedisTemplate<String, Object> redisTemplate;

	public Map<Object, Object> entries(String key) {
		return redisTemplate.opsForHash().entries(key);
	}

}
