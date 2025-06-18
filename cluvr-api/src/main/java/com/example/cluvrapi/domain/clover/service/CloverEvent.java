package com.example.cluvrapi.domain.clover.service;

import java.time.LocalDateTime;

import com.example.cluvrapi.domain.clover.enums.CloverUserActivityType;
import com.example.cluvrapi.domain.clover.listener.dto.CloverEventRequestDto;
import com.example.cluvrapi.global.event.enums.RedisKey;
import com.example.cluvrapi.global.event.enums.UserEventType;
import com.example.cluvrapi.global.event.service.UserEvent;

public class CloverEvent extends UserEvent<CloverEventRequestDto> {
	public CloverEvent(Long userId, RedisKey redisKey, UserEventType type, CloverEventRequestDto eventData) {
		super(userId, redisKey, type, eventData);
	}

	public static CloverEvent createEvent(Long userId, Integer clover, LocalDateTime createdTime,
		LocalDateTime deletedTime, CloverUserActivityType cloverUserActivityType) {
		return new CloverEvent(userId, RedisKey.CLOVER_LOG, UserEventType.CLOVER,
			CloverEventRequestDto.of(clover, createdTime, deletedTime, cloverUserActivityType.getDescription(), userId,
				cloverUserActivityType.name(), cloverUserActivityType.getFlowType())
		);
	}
}
