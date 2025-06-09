package com.example.cluvrapi.domain.analytics.dto;

import lombok.Getter;

@Getter
public class CategoryStatRedisDto {

	private Integer totalAnswer;

	private Integer totalSelected;

	private Integer totalScore;

	private Integer totalQuestion;

	private Long userId;

	private Long categoryId;

	public CategoryStatRedisDto(Integer totalAnswer, Integer totalSelected, Integer totalScore, Integer totalQuestion,
		Long userId, Long categoryId) {
		this.totalAnswer = totalAnswer;
		this.totalSelected = totalSelected;
		this.totalScore = totalScore;
		this.totalQuestion = totalQuestion;
		this.userId = userId;
		this.categoryId = categoryId;
	}

}
