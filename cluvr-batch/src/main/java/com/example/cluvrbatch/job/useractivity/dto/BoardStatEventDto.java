package com.example.cluvrbatch.job.useractivity.dto;

import static lombok.AccessLevel.PROTECTED;

import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.cluvrbatch.job.useractivity.enums.Tier;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class BoardStatEventDto { // redis에 올릴 데이터

	private Long userId;
	private Integer totalAnswer;
	private Integer totalScore;
	private Integer totalSelected;
	private Integer totalQuestion;
	private Tier tier;

	public BoardStatEventDto(Long userId, Integer totalAnswer, Integer totalScore, Integer totalSelected,
		Integer totalQuestion, Tier tier) {
		this.userId = userId;
		this.totalAnswer = totalAnswer;
		this.totalScore = totalScore;
		this.totalSelected = totalSelected;
		this.totalQuestion = totalQuestion;
		this.tier = tier;
	}

	public static BoardStatEventDto of(Long userId, Integer totalAnswer, Integer totalScore, Integer totalSelected,
		Integer totalQuestion, Tier tier) {
		return new BoardStatEventDto(userId, totalAnswer, totalScore, totalSelected, totalQuestion, tier);
	}

	public void updateTier(Tier tier) {
		this.tier = tier;
	}
}

