package com.example.cluvrapi.domain.gem.service;

import java.time.LocalDateTime;

import com.example.cluvrapi.domain.gem.enums.GemActionType;
import com.example.cluvrapi.domain.gem.listener.dto.GemEventDto;
import com.example.cluvrapi.global.event.enums.RedisKey;
import com.example.cluvrapi.global.event.enums.UserEventType;
import com.example.cluvrapi.global.event.service.UserEvent;

public class GemEvent extends UserEvent<GemEventDto> {
	public GemEvent(Long userId, RedisKey redisKey, UserEventType type, GemEventDto dto) {
		super(userId, redisKey, type, dto);
	}

	public static GemEvent createEvent(Long userId, Integer gem, String description, LocalDateTime createdTime,
		LocalDateTime deletedTime, GemActionType flowType, String action) {
		return new GemEvent(userId, RedisKey.GEM_LOG, UserEventType.GEM,
			GemEventDto.of(userId, gem, description, createdTime, deletedTime, flowType, action)
		);
	}
}
