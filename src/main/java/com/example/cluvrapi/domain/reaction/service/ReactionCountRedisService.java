package com.example.cluvrapi.domain.reaction.service;

import java.time.Duration;

import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.cluvrapi.domain.board.entity.Board;
import com.example.cluvrapi.domain.reaction.enums.ReactionType;
import com.example.cluvrapi.domain.reaction.repository.ReactionRepository;
import com.example.cluvrapi.domain.reply.entity.Reply;

@Service
@RequiredArgsConstructor
public class ReactionCountRedisService {
	private final RedisTemplate<String, Long> redisTemplate;
	private static final Duration TTL = Duration.ofHours(24);
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
		String key = generateKey(targetType, id, type);
		ensureKeyExists(key, targetType, id, type);
		redisTemplate.opsForValue().increment(key);
	}

	private void decreaseReactionCount(String targetType, Long id, ReactionType type) {
		String key = generateKey(targetType, id, type);
		ensureKeyExists(key, targetType, id, type);
		redisTemplate.opsForValue().decrement(key);
	}

	private void ensureKeyExists(String key, String targetType, Long id, ReactionType type) {
		if (!redisTemplate.hasKey(key)) {
			long count = switch (targetType) {
				case "board" -> reactionRepository.countBoardReactions(id, type);
				case "reply" -> reactionRepository.countReplyReactions(id, type);
				default -> throw new IllegalArgumentException("Unknown targetType: " + targetType);
			};
			redisTemplate.opsForValue().set(key, count, TTL);
		}
	}

	private String generateKey(String targetType, Long id, ReactionType type) {
		return String.format("reaction:%s:%s:%d", targetType.toLowerCase(), type.name().toLowerCase(), id);
	}
}
