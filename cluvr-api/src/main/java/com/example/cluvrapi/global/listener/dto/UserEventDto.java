package com.example.cluvrapi.global.listener.dto;

import lombok.Getter;

import com.example.cluvrapi.global.listener.enums.RedisKey;
import com.example.cluvrapi.global.listener.enums.UserEventType;

@Getter
public class UserEventDto<T> {

	private final Long userId;

	private final UserEventType type;

	private final RedisKey redisKey;

	private final T dto;

	public UserEventDto(Long userId, RedisKey redisKey, UserEventType type, T dto) {
		this.userId = userId;
		this.type = type;
		this.dto = dto;
		this.redisKey = redisKey;
	}
}
