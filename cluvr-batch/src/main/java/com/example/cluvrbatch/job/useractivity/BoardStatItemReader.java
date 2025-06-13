package com.example.cluvrbatch.job.useractivity;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import lombok.RequiredArgsConstructor;

import org.springframework.batch.item.ItemReader;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.example.cluvrbatch.job.enums.RedisKey;
import com.example.cluvrbatch.job.useractivity.dto.BoardStatEventDto;
import com.example.cluvrbatch.job.useractivity.enums.Tier;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class BoardStatItemReader implements ItemReader<BoardStatEventDto> {

	private final RedisTemplate<String, String> redisTemplate;
	private final ObjectMapper objectMapper;

	private Queue<BoardStatEventDto> cached = new LinkedList<>();

	@Override
	public BoardStatEventDto read() throws Exception {
		if (cached.isEmpty()) {
			Set<String> keys = redisTemplate.keys(RedisKey.BOARD_ACTIVITY_LOG.getKey() + "*");

			for (String key : keys) {
				List<String> logs = redisTemplate.opsForList().range(key, 0, -1);
				for (String json : logs) {
					BoardStatEventDto dto = objectMapper.readValue(json, BoardStatEventDto.class);
					Tier tier = Tier.checkAndUpgrade(
						dto.getTier().getRevel(),
						dto.getTotalScore(),
						dto.getTotalQuestion(),
						dto.getTotalAnswer()
					);
					dto.updateTier(tier); // 통계데이터 바탕으로 티어 최신화
					cached.add(dto);
				}
				redisTemplate.delete(key); // 삭제
			}
		}

		return cached.poll(); // null 반환 시 자동 종료
	}
}
