package com.example.cluvrapi.global.listener.dto;

import lombok.Getter;

import com.example.cluvrapi.global.listener.enums.UserEventType;

@Getter
public class UserEventDto<T> {

	private Long userId;

	private UserEventType type;

	private T dto;

	public UserEventDto(Long userId, UserEventType type, T dto) {
		this.userId = userId;
		this.type = type;
		this.dto = dto;
	}
}
