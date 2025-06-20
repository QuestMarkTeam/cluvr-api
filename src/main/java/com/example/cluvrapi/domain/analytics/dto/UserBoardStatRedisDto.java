package com.example.cluvrapi.domain.analytics.dto;

import lombok.Getter;

@Getter
public class UserBoardStatRedisDto {

	private Integer score;

	private Long userId;

	private Long categoryId;

	public UserBoardStatRedisDto(Integer score, Long userId, Long categoryId) {
		this.score = score;
		this.userId = userId;
		this.categoryId = categoryId;
	}
}
