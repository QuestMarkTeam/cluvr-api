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

	private static final String VIEW_USER_KEY = "board:%d:views:%d";
	private static final String VIEW_COUNT_KEY = "board:%d:views";

	public boolean hasUserKey(long boardId, long userId) {
		String key = String.format(VIEW_USER_KEY, boardId, userId);
		return redisTemplate.hasKey(key);
	}

	public long getViewCountFromRedis(Board board) {
		String viewCountKey = buildViewCountKey(board.getId());

		Long count = redisTemplate.opsForValue().get(viewCountKey);
		if (count == null) {
			redisTemplate.opsForValue().set(viewCountKey, board.getViewCount());
			return board.getViewCount();
		}
		return count;
	}

	public void incrementViewCount(Board board, Long userId) {
		String viewCountKey = buildViewCountKey(board.getId());
		String viewUserKey = buildViewUserKey(board.getId(), userId);

		if (!redisTemplate.hasKey(viewCountKey)) {
			initializeKeys(viewCountKey, viewUserKey, board.getViewCount() + 1, userId);
			return;
		}

		increaseViewCount(viewCountKey, viewUserKey, userId);
	}

	private String buildViewCountKey(Long boardId) {
		return String.format(VIEW_COUNT_KEY, boardId);
	}

	private String buildViewUserKey(Long boardId, Long userId) {
		return String.format(VIEW_USER_KEY, boardId, userId);
	}

	private void initializeKeys(String viewCountKey, String viewUserKey, long initialCount, Long userId) {
		redisTemplate.opsForValue().set(viewCountKey, initialCount);
		redisTemplate.expire(viewCountKey, TTL);

		redisTemplate.opsForSet().add(viewUserKey, userId);
		redisTemplate.expire(viewUserKey, TTL);
	}

	private void increaseViewCount(String viewCountKey, String viewUserKey, Long userId) {
		redisTemplate.opsForSet().add(viewUserKey, userId);
		redisTemplate.expire(viewUserKey, TTL);

		redisTemplate.opsForValue().increment(viewCountKey);
	}
}
