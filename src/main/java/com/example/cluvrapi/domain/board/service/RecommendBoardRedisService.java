package com.example.cluvrapi.domain.board.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.RedisSystemException;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.cluvrapi.domain.category.enums.CategoryType;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendBoardRedisService {
	// 추후 자료구조 변경: zset, sortedSet
	private final RedisTemplate<String, Long> redisTemplate;

	private static final String REDIS_KEY_FORMAT = "recommend:board:%s:%d";
	private static final String REDIS_KEY_FORMAT_ALL = "recommend:board:%s:*";

	public void updateRecommendBoard(CategoryType categoryType, long boardId) {
		try {
			String key = String.format(REDIS_KEY_FORMAT, categoryType.name().toLowerCase(), boardId);
			if (!redisTemplate.hasKey(key)) {
				redisTemplate.opsForValue().set(key, 0L);
			}

			redisTemplate.opsForValue().increment(key);
		} catch (RedisSystemException e) {
			// Redis 연결 실패 시 로깅만 하고 계속 진행
			log.error("Redis operation failed: {}", e.getMessage());
		}
	}

	public List<Long> getRecommendedBoardFromRedis(CategoryType categoryType) {
		Set<String> keys = redisTemplate.keys(String.format(REDIS_KEY_FORMAT_ALL, categoryType.name().toLowerCase()));
		if (keys == null || keys.isEmpty()) {
			return List.of(); // 빈 리스트 반환
		}

		List<long[]> boardScores = new ArrayList<>();
		for (String key : keys) {
			Long score = redisTemplate.opsForValue().get(key);
			if (score == null)
				continue;

			// 키 포맷: recommend:board:{category}:{boardId}
			String[] parts = key.split(":");
			if (parts.length != 4)
				continue;

			try {
				long boardId = Long.parseLong(parts[3]); // 키에서 boardId만 추출
				boardScores.add(new long[] {boardId, score});
			} catch (NumberFormatException ignored) {
			}
		}

		// 점수 기준 내림차순 정렬
		boardScores.sort((a, b) -> Long.compare(b[1], a[1]));

		// boardId만 추출하고 상위 5개만 반환
		return boardScores.stream()
			.map(a -> a[0])
			.limit(5)
			.collect(Collectors.toList());
	}
}
