package com.example.cluvrapi.domain.board.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateBoardRequestDto {

	private final String title;

	private final String content;

	@Min(value = 0, message = " 클로버는 0~ 100이하로만 추가할 수 있습니다.")
	@Max(value = 100, message = "클로버는 0~ 100이하로만 추가할 수 있습니다.")
	private final int clover;
}
