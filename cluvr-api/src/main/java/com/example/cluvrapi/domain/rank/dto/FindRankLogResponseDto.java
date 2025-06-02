package com.example.cluvrapi.domain.rank.dto;

import java.time.LocalDateTime;

import lombok.Getter;

import com.querydsl.core.annotations.QueryProjection;

@Getter
public class FindRankLogResponseDto {

	private final Long userId;

	private final String description;

	private final Integer amount;

	private final LocalDateTime createdAt;
	
	private final LocalDateTime deletedAt;

	@QueryProjection
	public FindRankLogResponseDto(Long userId, String description, Integer amount, LocalDateTime createdAt,
		LocalDateTime deletedAt) {
		this.userId = userId;
		this.description = description;
		this.amount = amount;
		this.createdAt = createdAt;
		this.deletedAt = deletedAt;
	}
}
