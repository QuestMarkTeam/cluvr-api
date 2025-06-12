package com.example.cluvrapi.global.event.service;

import lombok.Getter;

import com.example.cluvrapi.global.event.enums.RedisKey;
import com.example.cluvrapi.global.event.enums.UserEventType;

@Getter
public class UserEvent<T> {

	private final Long userId;

	private final UserEventType type;

	private final RedisKey redisKey;

	private final T dto;

	public UserEvent(Long userId, RedisKey redisKey, UserEventType type, T dto) {
		this.userId = userId;
		this.type = type;
		this.dto = dto;
		this.redisKey = redisKey;
	}

	// 부모 클래스에서 이벤트 생성 로직을 처리
	public static <T> UserEvent<T> createEvent(Long userId, RedisKey redisKey, UserEventType eventType, T dto) {
		return new UserEvent<>(userId, redisKey, eventType, dto);
	}
}
