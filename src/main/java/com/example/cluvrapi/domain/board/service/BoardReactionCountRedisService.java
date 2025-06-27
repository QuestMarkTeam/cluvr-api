package com.example.cluvrapi.domain.board.service;

import java.time.Duration;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.cluvrapi.domain.board.entity.Board;
import com.example.cluvrapi.domain.reaction.enums.ReactionType;
import com.example.cluvrapi.domain.reaction.repository.ReactionRepository;

@RequiredArgsConstructor
@Slf4j
@Service
public class BoardReactionCountRedisService {
	private static final Duration TTL = Duration.ofHours(24);
	private final RedisTemplate<String, Long> redisTemplate;
	private final ReactionRepository reactionRepository;

	private static final String REDIS_KEY_FORMAT = "reaction:board:%s:%d";

	private long readReactionCountFromRedis(Board board, ReactionType type) {
		String key = String.format(REDIS_KEY_FORMAT, type.name().toLowerCase(), board.getId());

		try {
			if (!redisTemplate.hasKey(key)) {
				return setReactionToRedis(key, type, board.getId());
			}
			Long value = redisTemplate.opsForValue().get(key);
			return value != null ? value : setReactionToRedis(key, type, board.getId());
		} catch (Exception e) {
			log.warn("Redis {} 조회 실패: {}", type.name(), e.getMessage());
			return setReactionToRedis(key, type, board.getId());
		}
	}

	public long readLikeCountFromRedis(Board board) {
		return readReactionCountFromRedis(board, ReactionType.LIKE);
	}

	public long readDislikeCountFromRedis(Board board) {
		return readReactionCountFromRedis(board, ReactionType.DISLIKE);
	}

	private long setReactionToRedis(String key, ReactionType type, long boardId) {
		long count = reactionRepository.countBoardReactions(boardId, type);
		redisTemplate.opsForValue().set(key, count);
		redisTemplate.expire(key, TTL);
		return count;
	}
}
