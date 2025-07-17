package com.example.cluvrapi.domain.board.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisCallback;
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

	/**
	 * Redis Pipeline을 사용하여 여러 board의 viewCount를 한 번에 조회
	 */
	// public List<Long> getViewCountsFromRedis(List<Board> boards) {
	// 	List<String> keys = boards.stream()
	// 		.map(board -> buildViewCountKey(board.getId()))
	// 		.toList();
	//
	// 	// Redis Pipeline 사용
	// 	return redisLongTemplate.executePipelined((RedisCallback<Object>) connection -> {
	// 		List<Long> results = new ArrayList<>();
	// 		for (int i = 0; i < boards.size(); i++) {
	// 			Board board = boards.get(i);
	// 			String key = keys.get(i);
	//
	// 			// Redis에서 조회
	// 			byte[] value = connection.get(key.getBytes());
	// 			Long count = value != null ? Long.parseLong(new String(value)) : null;
	//
	// 			if (count == null) {					// Redis에 없으면 DB 값 + 1					count = board.getViewCount() + 1				connection.set(key.getBytes(), String.valueOf(count).getBytes());
	// 				connection.expire(key.getBytes(), TTL.toSeconds());
	// 			}
	//
	// 			results.add(count);
	// 		}
	// 		return results;
	// 	});
	// }

	private String buildViewCountKey(Long boardId) {
		return String.format(VIEW_COUNT_KEY, boardId);
	}

	private String buildViewUserKey(Long boardId) {
		return String.format(VIEW_USER_KEY, boardId);
	}
}
