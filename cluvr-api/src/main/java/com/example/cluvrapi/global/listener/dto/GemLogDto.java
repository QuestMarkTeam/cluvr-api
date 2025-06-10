package com.example.cluvrapi.global.listener.dto;

import java.time.LocalDateTime;

import lombok.Getter;

import com.example.cluvrapi.domain.gem.enums.GemFlowType;

@Getter
public class GemLogDto {
	private Long userId;
	private Integer amount;
	private String description;
	private LocalDateTime createdAt;
	private LocalDateTime deletedAt;
	private String action;
	private GemFlowType flowType;

	public GemLogDto(Long userId, Integer amount, String description, LocalDateTime createdAt,
		LocalDateTime deletedAt, String action, GemFlowType flowType) {
		this.userId = userId;
		this.amount = amount;
		this.description = description;
		this.createdAt = createdAt;
		this.deletedAt = deletedAt;
		this.action = action;
		this.flowType = flowType;
	}

	public static GemLogDto of(Long userId, Integer amount, String description,
		String action, GemFlowType flowType) {
		return new GemLogDto(userId, amount, description, LocalDateTime.now(), LocalDateTime.now(), action, flowType);
	}
}
