package com.example.cluvrapi.domain.clover.listener;

import lombok.RequiredArgsConstructor;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.example.cluvrapi.domain.clover.listener.dto.CloverEventRequestDto;
import com.example.cluvrapi.domain.clover.service.CloverEvent;
import com.example.cluvrapi.domain.clover.service.CloverEventRedisService;
import com.example.cluvrapi.domain.clover.service.CloverService;

@RequiredArgsConstructor
@Component
public class CloverEventListener {
	private final CloverEventRedisService redisService;
	private final CloverService cloverService;

	@Async
	@EventListener
	public void handleUserActivity(CloverEvent event) {
		CloverEventRequestDto eventDto = event.getDto();
		Long userId = event.getUserId();
		String redisKey = event.getRedisKey().buildKey(userId);
		double score = System.currentTimeMillis();

		// 이벤트에 해당하는 클로버 적립 로그 저장
		redisService.setZSetValue(redisKey, eventDto, score);
		redisService.setExpireOfDay(redisKey, 31L);
	}
}
