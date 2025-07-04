package com.example.cluvrapi.domain.board.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.cluvrapi.domain.board.entity.Board;

@Service
public class BoardViewCountRedisService {

	private final RedisTemplate<String, String> redisTemplate;
	private final RedisTemplate<String, Long> redisLongTemplate;

	private static final Duration TTL = Duration.ofHours(24);
	private static final String VIEW_USER_KEY = "board:%d:viewers";
	private static final String VIEW_COUNT_KEY = "board:%d:views";

	public BoardViewCountRedisService(
		@Autowired @Qualifier("redisStringTemplate") RedisTemplate<String, String> redisTemplate,
		@Autowired @Qualifier("redisCountViewTemplate") RedisTemplate<String, Long> redisLongTemplate) {
		this.redisTemplate = redisTemplate;
		this.redisLongTemplate = redisLongTemplate;
	}

	public boolean isFirst(Board board, long userId) {
		String viewUserKey = buildViewUserKey(board.getId());

		// 중복일 시 리턴
		if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(viewUserKey, String.valueOf(userId)))) {
			return false;
		}

		redisTemplate.opsForSet().add(viewUserKey, String.valueOf(userId));
		redisTemplate.expire(viewUserKey, TTL);
		return true;
	}

	public long getViewCountFromRedis(Board board, boolean isFirst) {
		String viewCountKey = buildViewCountKey(board.getId());

		Long countStr = redisLongTemplate.opsForValue().get(viewCountKey);

		if (countStr == null) {
			redisLongTemplate.opsForValue().set(viewCountKey, board.getViewCount() + 1);
			System.out.println(redisLongTemplate.opsForValue().get(viewCountKey));
			return board.getViewCount() + 1;
		}
		if (isFirst){
			redisLongTemplate.opsForValue().increment(viewCountKey);
		}

		return countStr + 1;
	}

	private String buildViewCountKey(Long boardId) {
		return String.format(VIEW_COUNT_KEY, boardId);
	}

	private String buildViewUserKey(Long boardId) {
		return String.format(VIEW_USER_KEY, boardId);
	}
}
