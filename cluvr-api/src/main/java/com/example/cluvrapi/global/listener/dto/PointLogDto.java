package com.example.cluvrapi.global.listener.dto;

import java.time.LocalDateTime;

import lombok.Getter;

import com.example.cluvrapi.domain.point.enums.PointFlowType;

@Getter
public class PointLogDto {
	private Long userId;
	private Integer amount;
	private String description;
	private LocalDateTime createdAt;
	private LocalDateTime deletedAt;
	private String action;
	private PointFlowType flowType;

	public PointLogDto(Long userId, Integer amount, String description, LocalDateTime createdAt,
		LocalDateTime deletedAt, String action, PointFlowType flowType) {
		this.userId = userId;
		this.amount = amount;
		this.description = description;
		this.createdAt = createdAt;
		this.deletedAt = deletedAt;
		this.action = action;
		this.flowType = flowType;
	}

	public static PointLogDto of(Long userId, Integer amount, String description,
		String action, PointFlowType flowType) {
		return new PointLogDto(userId, amount, description, LocalDateTime.now(), LocalDateTime.now(), action, flowType);
	}
}
