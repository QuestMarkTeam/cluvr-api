package com.example.cluvrapi.domain.reaction.service;

import java.time.Duration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.cluvrapi.domain.board.entity.Board;
import com.example.cluvrapi.domain.reaction.enums.ReactionType;
import com.example.cluvrapi.domain.reaction.repository.ReactionRepository;
import com.example.cluvrapi.domain.reply.entity.Reply;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReactionCountRedisService {
	private static final Duration TTL = Duration.ofHours(24);
	private final RedisTemplate<String, Object> redisTemplate;
	private final ReactionRepository reactionRepository;

	public void increaseBoardReactionCount(ReactionType type, Board board) {
		increaseReactionCount("board", board.getId(), type);
	}

	public void decreaseBoardReactionCount(ReactionType type, Board board) {
		decreaseReactionCount("board", board.getId(), type);
	}

	public void increaseReplyReactionCount(ReactionType type, Reply reply) {
		increaseReactionCount("reply", reply.getId(), type);
	}

	public void decreaseReplyReactionCount(ReactionType type, Reply reply) {
		decreaseReactionCount("reply", reply.getId(), type);
	}

	private void increaseReactionCount(String targetType, Long id, ReactionType type) {
		String key = generateKey(targetType, id);
		String hashKey = type.name().toLowerCase();
		ensureHashKeyExists(key, hashKey, targetType, id, type);
		redisTemplate.opsForHash().increment(key, hashKey, 1);
		redisTemplate.expire(key, TTL);
	}

	private void decreaseReactionCount(String targetType, Long id, ReactionType type) {
		String key = generateKey(targetType, id);
		String hashKey = type.name().toLowerCase();
		ensureHashKeyExists(key, hashKey, targetType, id, type);
		
		// 현재 값을 가져와서 0보다 클 때만 감소
		HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
		Object currentValue = hashOps.get(key, hashKey);
		long currentCount = 0;
		
		if (currentValue instanceof Number) {
			currentCount = ((Number) currentValue).longValue();
		}
		
		// 0보다 클 때만 감소
		if (currentCount > 0) {
			hashOps.increment(key, hashKey, -1);
		}
		
		redisTemplate.expire(key, TTL);
	}

	private void ensureHashKeyExists(String key, String hashKey, String targetType, Long id, ReactionType type) {
		HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
		if (!hashOps.hasKey(key, hashKey)) {
			long count = switch (targetType) {
				case "board" -> reactionRepository.countBoardReactions(id, type);
				case "reply" -> reactionRepository.countReplyReactions(id, type);
				default -> throw new IllegalArgumentException("Unknown targetType: " + targetType);
			};
			// putIfAbsent은 동시성-safe
			hashOps.putIfAbsent(key, hashKey, count);
			redisTemplate.expire(key, TTL);
		}
	}

	public long readBoardReactionCount(Board board, ReactionType type) {
		return readReactionCount("board", board.getId(), type);
	}

	public long readReplyReactionCount(Reply reply, ReactionType type) {
		return readReactionCount("reply", reply.getId(), type);
	}

	private long readReactionCount(String targetType, Long id, ReactionType type) {
		String key = generateKey(targetType, id);
		String hashKey = type.name().toLowerCase();
		HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();

		try {
			Object value = hashOps.get(key, hashKey);
			if (value instanceof Number) {
				long count = ((Number) value).longValue();
				// 음수 값이면 데이터베이스에서 다시 조회하여 정정
				if (count < 0) {
					log.warn("Redis에서 음수 값 발견: {}:{} = {}", targetType, type.name(), count);
					return setReactionToRedis(key, hashKey, targetType, id, type);
				}
				return count;
			} else {
				return setReactionToRedis(key, hashKey, targetType, id, type);
			}
		} catch (Exception e) {
			log.warn("Redis {}:{} 조회 실패: {}", targetType, type.name(), e.getMessage());
			return setReactionToRedis(key, hashKey, targetType, id, type);
		}
	}

	private long setReactionToRedis(String key, String hashKey, String targetType, Long id, ReactionType type) {
		long count = switch (targetType) {
			case "board" -> reactionRepository.countBoardReactions(id, type);
			case "reply" -> reactionRepository.countReplyReactions(id, type);
			default -> throw new IllegalArgumentException("Unknown targetType: " + targetType);
		};
		System.out.println(redisTemplate.getConnectionFactory().getConnection().isClosed());
		redisTemplate.opsForHash().put(key, hashKey, count);
		redisTemplate.expire(key, TTL);
		return count;
	}

	private String generateKey(String targetType, Long id) {
		return String.format("reaction:%s:%d", targetType.toLowerCase(), id);
	}

	/**
	 * Redis에서 잘못된 리액션 카운트를 초기화하는 메서드
	 * @param targetType "board" 또는 "reply"
	 * @param id 대상 ID
	 */
	public void resetReactionCount(String targetType, Long id) {
		String key = generateKey(targetType, id);
		HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
		
		// LIKE와 DISLIKE 모두 초기화
		hashOps.delete(key, "like", "dislike");
		
		// 데이터베이스에서 정확한 값으로 다시 설정
		ensureHashKeyExists(key, "like", targetType, id, ReactionType.LIKE);
		ensureHashKeyExists(key, "dislike", targetType, id, ReactionType.DISLIKE);
		
		log.info("Redis 리액션 카운트 초기화 완료: {}:{}", targetType, id);
	}
}
