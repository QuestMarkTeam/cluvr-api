package com.example.cluvrapi.domain.board.service;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.cluvrapi.domain.analytics.entity.UserBoardStat;
import com.example.cluvrapi.domain.analytics.repository.UserBoardStatRepository;
import com.example.cluvrapi.domain.board.listener.dto.BoardEventRequestDto;
import com.example.cluvrapi.domain.clover.enums.Tier;
import com.example.cluvrapi.global.event.enums.RedisKey;
import com.example.cluvrapi.global.event.service.EventRedisService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BoardEventRedisService extends EventRedisService {

	private final RedisTemplate<String, Object> redisTemplate;
	private final UserBoardStatRepository repository;

	public BoardEventRedisService(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper,
		UserBoardStatRepository repository) {
		super(redisTemplate, objectMapper);
		this.redisTemplate = redisTemplate;
		this.repository = repository;
	}

	// 하드코딩 나중에 최적화 예정
	public void incrementHashValue(Long userId, String mainKey, BoardEventRequestDto eventDto) {
		List<RedisKey> keys = List.of(
			RedisKey.TOTAL_ANSWER,
			RedisKey.TOTAL_SELECTED,
			RedisKey.TOTAL_QUESTION,
			RedisKey.TOTAL_CLOVER,
			RedisKey.USER_TIER
		);
		initializeIfAbsent(userId, mainKey, keys);
		redisTemplate.opsForHash().increment(mainKey, keys.get(0).getKey(), eventDto.getAnswer());
		redisTemplate.opsForHash().increment(mainKey, keys.get(1).getKey(), eventDto.getSelected());
		redisTemplate.opsForHash().increment(mainKey, keys.get(2).getKey(), eventDto.getQuestion());
		redisTemplate.opsForHash().increment(mainKey, keys.get(3).getKey(), eventDto.getClover());
	}

	// 초기 값 세팅 하드코딩 나중에 최적화 예정
	private void initializeIfAbsent(Long userId, String mainKey, List<RedisKey> keys) {
		if (Boolean.FALSE.equals(redisTemplate.hasKey(mainKey))) {
			Map<String, Object> initialMap = new HashMap<>();
			Optional<UserBoardStat> optionalUserBoardStat = repository.findByUserId(userId); // 기존에 저장한 값이 있다면 가져와서 세팅
			if (optionalUserBoardStat.isPresent()) {
				UserBoardStat userBoardStat = optionalUserBoardStat.get();
				initialMap.put(keys.get(0).getKey(), userBoardStat.getTotalAnswer());
				initialMap.put(keys.get(1).getKey(), userBoardStat.getTotalSelected());
				initialMap.put(keys.get(2).getKey(), userBoardStat.getTotalQuestion());
				initialMap.put(keys.get(3).getKey(), userBoardStat.getTotalClover());
				initialMap.put(keys.get(4).getKey(), userBoardStat.getTier().name());
			} else {
				initialMap.put(keys.get(0).getKey(), 0);
				initialMap.put(keys.get(1).getKey(), 0);
				initialMap.put(keys.get(2).getKey(), 0);
				initialMap.put(keys.get(3).getKey(), 0);
				initialMap.put(keys.get(4).getKey(), Tier.SPROUT.name());
			}

			redisTemplate.opsForHash().putAll(mainKey, initialMap);
			redisTemplate.expire(mainKey, Duration.ofDays(1));
		}
	}

}
