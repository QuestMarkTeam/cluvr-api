package com.example.cluvrapi.domain.board.listener.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.cluvrapi.domain.clover.enums.Tier;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BoardEventRequestDto { // redis에 올릴 데이터

	private Long userId;
	private Integer answer = 0;
	private Integer clover = 0;
	private Integer selected = 0;
	private Integer question = 0;
	private Tier tier; // 티어

	public BoardEventRequestDto(Long userId, Integer answer, Integer clover, Integer selected,
		Integer question, Tier tier) {
		this.userId = userId;
		this.answer = answer;
		this.clover = clover;
		this.selected = selected;
		this.question = question;
		this.tier = tier;
	}

	public static BoardEventRequestDto of(Long userId, Integer answer, Integer clover, Integer selected,
		Integer question, Tier tier) {
		return new BoardEventRequestDto(userId, answer, clover, selected, question, tier);
	}
}
