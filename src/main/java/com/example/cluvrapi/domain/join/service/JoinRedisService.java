package com.example.cluvrapi.domain.join.service;

import java.util.Map;

import lombok.RequiredArgsConstructor;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinRedisService {
	private final RedisTemplate<String, Object> redisTemplate;
	private final RedissonClient redissonClient;

	private static final String CLUB_JOIN_LOCK_PREFIX = "club_join_lock:";

	public Map<Object, Object> entries(String key) {
		return redisTemplate.opsForHash().entries(key);
	}

	public RLock getWriteLock(Long clubId) {
		String lockKey = CLUB_JOIN_LOCK_PREFIX + clubId;
		return redissonClient.getReadWriteLock(lockKey).writeLock();
	}
}
