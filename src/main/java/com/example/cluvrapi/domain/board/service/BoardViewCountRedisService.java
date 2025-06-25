package com.example.cluvrapi.domain.board.service;

import java.time.Duration;

import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.cluvrapi.domain.board.entity.Board;

@Service
@RequiredArgsConstructor
public class BoardViewCountRedisService {
	private final RedisTemplate<String, Long> redisTemplate;
	private static final Duration TTL = Duration.ofHours(24);

	public long incrementViewCount(Board board, Long userId) {
		String viewUserKey = "board:viewed:" + board.getId();
		String viewCountKey = "board:" + board.getId() + ":views";

		// 이미 본 유저면 그냥 현재 값 반환
		Boolean isMember = redisTemplate.opsForSet().isMember(viewUserKey, userId);
		if (Boolean.TRUE.equals(isMember)) {
			return safeLong(redisTemplate.opsForValue().get(viewCountKey), board.getViewCount());
		}

		// Redis에 조회수 키 없으면 초기화
		if (Boolean.FALSE.equals(redisTemplate.hasKey(viewCountKey))) {
			long initialCount = board.getViewCount() + 1;
			redisTemplate.opsForValue().set(viewCountKey, initialCount);
			redisTemplate.expire(viewCountKey, TTL);

			redisTemplate.opsForSet().add(viewUserKey, userId);
			redisTemplate.expire(viewUserKey, TTL);

			return initialCount;
		}

		// 유저 조회 기록 추가 + 뷰 카운트 증가
		redisTemplate.opsForSet().add(viewUserKey, userId);
		redisTemplate.expire(viewUserKey, TTL);

		Long value = redisTemplate.opsForValue().increment(viewCountKey);
		return safeLong(value, board.getViewCount() + 1);
	}

	private long safeLong(Long value, long fallback) {
		return value != null ? value : fallback;
	}
}
