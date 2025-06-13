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

	// 초대코드 정보를 Redis Hash 에 저장하고 TTL(Time To Live)을 설정합니다.
	public void saveInviteCode(String key, Map<String, Object> codeData, Duration validityTime) {
		redisTemplate.opsForHash().putAll(key, codeData);
		redisTemplate.expire(key, validityTime.getSeconds(), TimeUnit.SECONDS);
	}

	public boolean hasKey(String key) {
		return redisTemplate.hasKey(key);
	}
}
