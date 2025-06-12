package com.example.cluvrapi.domain.clover.listener.dto;

import java.time.LocalDateTime;

import lombok.Getter;

import com.example.cluvrapi.domain.clover.enums.CloverActionType;

@Getter
public class CloverEventDto { // redis에 올릴 데이터

	private final Long userId;
	private final Integer amount;
	private final LocalDateTime createdAt;
	private final LocalDateTime deletedAt;
	private final String description;
	private final String action; // 어떤 활동으로 적립인지
	private final CloverActionType flowType; // 사용인지 적립인지

	public CloverEventDto(Integer amount, LocalDateTime createdAt, LocalDateTime deletedAt, String description,
		Long userId, String action, CloverActionType flowType) {
		this.amount = amount;
		this.createdAt = createdAt;
		this.deletedAt = deletedAt;
		this.description = description;
		this.userId = userId;
		this.action = action;
		this.flowType = flowType;
	}

	public static CloverEventDto of(Integer amount, LocalDateTime createdAt, LocalDateTime deletedAt,
		String description,
		Long userId, String action, CloverActionType flowType) {
		return new CloverEventDto(amount, createdAt, deletedAt, description, userId, action,
			flowType);
	}

}
