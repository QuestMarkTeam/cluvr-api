package com.example.cluvrbatch.job.gemlog.dto;

import java.time.LocalDateTime;

import lombok.Getter;

import com.example.cluvrbatch.job.gemlog.enums.GemActionType;

@Getter
public class GemEventDto {
	private final Long userId;
	private final Integer amount;
	private final String description;
	private final LocalDateTime createdAt;
	private final LocalDateTime deletedAt;
	private final GemActionType flowType;
	private final String action;

	// 30일간 기준으로 최신 gem 로그를 보여주기위해 레디스에 저장
	public GemEventDto(Long userId, Integer amount, String description, LocalDateTime createdAt,
		LocalDateTime deletedAt, GemActionType flowType, String action) {
		this.userId = userId;
		this.amount = amount;
		this.description = description;
		this.createdAt = createdAt;
		this.deletedAt = deletedAt;
		this.flowType = flowType;
		this.action = action;
	}

	public static GemEventDto of(Long userId, Integer amount, String description, LocalDateTime createdAt,
		LocalDateTime deletedAt, GemActionType flowType, String action) {
		return new GemEventDto(userId, amount, description, createdAt, deletedAt, flowType,
			action);
	}
}
