package com.example.cluvrapi.domain.club.service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClubRedisService {

	private final RedisTemplate<String, Object> redisTemplate;

	// 키가 없으면 값을 설정하고 true 반환, 키가 이미 존재하면 아무 작업도 하지 않고 false 반환
	public void saveInviteCode(String key, Map<String, Object> codeData, Duration validityTime) {
		redisTemplate.opsForHash().putAll(key, codeData);
		redisTemplate.expire(key, validityTime.getSeconds(), TimeUnit.DAYS);
	}

	public boolean hasKey(String key) {
		return redisTemplate.hasKey(key);
	}
}
