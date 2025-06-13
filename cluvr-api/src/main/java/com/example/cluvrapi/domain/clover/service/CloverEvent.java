package com.example.cluvrapi.domain.clover.service;

import java.time.LocalDateTime;

import com.example.cluvrapi.domain.clover.enums.CloverUserActivityType;
import com.example.cluvrapi.domain.clover.listener.dto.CloverEventDto;
import com.example.cluvrapi.global.event.enums.RedisKey;
import com.example.cluvrapi.global.event.enums.UserEventType;
import com.example.cluvrapi.global.event.service.UserEvent;

public class CloverEvent extends UserEvent<CloverEventDto> {
	public CloverEvent(Long userId, RedisKey redisKey, UserEventType type, CloverEventDto eventData) {
		super(userId, redisKey, type, eventData);
	}

	public static CloverEvent createEvent(Long userId, Integer clover, LocalDateTime createdTime,
		LocalDateTime deletedTime, CloverUserActivityType cloverUserActivityType) {
		return new CloverEvent(userId, RedisKey.CLOVER_LOG, UserEventType.CLOVER,
			CloverEventDto.of(clover, createdTime, deletedTime, cloverUserActivityType.getDescription(), userId,
				cloverUserActivityType.name(), cloverUserActivityType.getFlowType())
		);
	}
}
