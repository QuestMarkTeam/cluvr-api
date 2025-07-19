package com.example.cluvrapi.domain.board.service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.RedisSystemException;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import com.example.cluvrapi.domain.board.dto.response.ReadAllBoardsResponseDto;
import com.example.cluvrapi.domain.category.enums.CategoryType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Slf4j
public class RecommendBoardRedisService {
	private final RedisTemplate<String, String> redisTemplate;
	private final ObjectMapper objectMapper;

	private static final String REDIS_KEY_FORMAT = "recommend:board:%s";
	private static final long TTL_SECONDS = 60 * 60 * 24; // 24시간

	public RecommendBoardRedisService(@Qualifier("redisStringTemplate") RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
		this.redisTemplate = redisTemplate;
		this.objectMapper = objectMapper;
	}

	/**
	 * 추천수 증가
	 */
	public void updateRecommendBoard(CategoryType categoryType, long boardId, boolean isFirst) {
		String key = String.format(REDIS_KEY_FORMAT, categoryType.name().toLowerCase());

		redisTemplate.opsForZSet().incrementScore(key, String.valueOf(boardId), 1.0);

		// TTL 설정 (기존 TTL이 없을 경우만 설정, 반복 설정 방지)
		Long expire = redisTemplate.getExpire(key);
		if (expire <= 0) {
			redisTemplate.expire(key, TTL_SECONDS, TimeUnit.SECONDS);
		}

	}

	/**
	 * 상위 5개 추천 게시글 가져오기 (score 기준 내림차순)
	 */
	public List<Long> getRecommendedBoardFromRedis(CategoryType categoryType) {
		String key = String.format(REDIS_KEY_FORMAT, categoryType.name().toLowerCase());

		Set<ZSetOperations.TypedTuple<String>> resultSet = redisTemplate.opsForZSet()
			.reverseRangeWithScores(key, 0, 4); // 내림차순 상위 5개

		if (resultSet == null || resultSet.isEmpty()) {
			return List.of();
		}

		return resultSet.stream()
			.map(ZSetOperations.TypedTuple::getValue).filter(Objects::nonNull)
			.map(Long::parseLong)
			.collect(Collectors.toList());
	}

	public List<ReadAllBoardsResponseDto> getRecommendedBoardsFromRedis(CategoryType categoryType) throws
		JsonProcessingException {
		String json = redisTemplate.opsForValue().get("board:popular:list");

		List<ReadAllBoardsResponseDto> dtos =  objectMapper.readValue(json,
			new TypeReference<List<ReadAllBoardsResponseDto>>() {
			});
		return dtos;
	}
}
