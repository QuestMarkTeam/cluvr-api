package com.example.cluvrapi.domain.reply.service;

import java.time.Duration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.cluvrapi.domain.board.entity.Board;
import com.example.cluvrapi.domain.reaction.enums.ReactionType;
import com.example.cluvrapi.domain.reaction.repository.ReactionRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReplyReactionRedisService {
	private static final Duration TTL = Duration.ofHours(24);
	private final RedisTemplate<String, Long> redisTemplate;
	private final ReactionRepository reactionRepository;

	private static final String REDIS_KEY_FORMAT = "reaction:reply:%s:%d";

	private long readReactionCountFromRedis(long replyId, ReactionType type) {
		String key = String.format(REDIS_KEY_FORMAT, type.name().toLowerCase(), replyId);

		try {
			if (!redisTemplate.hasKey(key)) {
				return setReactionToRedis(key, type, replyId);
			}
			Long value = redisTemplate.opsForValue().get(key);
			return value != null ? value : setReactionToRedis(key, type, replyId);
		} catch (Exception e) {
			log.warn("Redis {} 조회 실패: {}", type.name(), e.getMessage());
			return setReactionToRedis(key, type, replyId);
		}
	}

	public long readLikeCountFromRedis(long replyId) {
		return readReactionCountFromRedis(replyId, ReactionType.LIKE);
	}

	public long readDislikeCountFromRedis(long replyId) {
		return readReactionCountFromRedis(replyId, ReactionType.DISLIKE);
	}

	private long setReactionToRedis(String key, ReactionType type, long replyId) {
		long count = reactionRepository.countReplyReactions(replyId, type);
		redisTemplate.opsForValue().set(key, count);
		redisTemplate.expire(key, TTL);
		return count;
	}
}
