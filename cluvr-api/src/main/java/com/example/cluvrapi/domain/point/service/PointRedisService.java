package com.example.cluvrapi.domain.point.service;

import java.time.Duration;

import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PointRedisService {
	private final RedisTemplate<String, Object> redisTemplate;

	// 키가 없으면 값을 설정하고 true 반환, 키가 이미 존재하면 아무 작업도 하지 않고 false 반환
	public boolean setIfAbsent(String key, Object value, Duration validityTime) {
		return Boolean.TRUE.equals(
			redisTemplate.opsForValue().setIfAbsent(key, value, validityTime));
	}

	public Long incrementValue(String key) {
		return redisTemplate.opsForValue().increment(key);
	}

}
