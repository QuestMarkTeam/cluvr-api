package com.example.cluvrapi.domain.club.service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 클럽 초대코드 관련 Redis 캐시 처리를 담당하는 서비스 클래스입니다.
 *
 * <p> Redis 를 활용하여 초대코드 정보를 Hash 형태로 저장하고, TTL(Time To Live)을 설정하여
 * 일정 시간이 지나면 자동으로 만료되도록 관리합니다.
 *
 * @author sinyoung0403
 */

@RequiredArgsConstructor
@Service
public class ClubRedisService {

	private final RedisTemplate<String, Object> redisTemplate;

	/**
	 * 설명: 초대코드 정보를 Redis Hash 에 저장하고 TTL 을 설정합니다.
	 *
	 * @param key          저장할 Redis 키
	 * @param codeData     초대코드 관련 데이터가 담긴 Map
	 * @param validityTime 데이터의 유효기간 (Duration 타입)
	 * @author sinyoung0403
	 */

	public void saveInviteCode(String key, Map<String, Object> codeData, Duration validityTime) {
		redisTemplate.opsForHash().putAll(key, codeData);
		redisTemplate.expire(key, validityTime.getSeconds(), TimeUnit.SECONDS);
	}

	/**
	 * 설명: Redis 에 특정 키가 존재하는지 여부를 확인합니다.
	 *
	 * @param key 확인할 Redis 키
	 * @return 키 존재 여부 (true: 존재, false: 없음)
	 * @author sinyoung0403
	 */

	public boolean hasKey(String key) {
		return redisTemplate.hasKey(key);
	}
}
