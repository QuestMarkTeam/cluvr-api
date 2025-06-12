package com.example.cluvrapi.domain.clover.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;

import com.querydsl.core.annotations.QueryProjection;

@Getter
public class FindCloverLogResponseDto {

	private final Long userId;

	private final String description;

	private final Integer amount;

	private final LocalDateTime createdAt;

	private final LocalDateTime deletedAt;

	@QueryProjection
	public FindCloverLogResponseDto(Long userId, String description, Integer amount, LocalDateTime createdAt,
		LocalDateTime deletedAt) {
		this.userId = userId;
		this.description = description;
		this.amount = amount;
		this.createdAt = createdAt;
		this.deletedAt = deletedAt;
	}
}
