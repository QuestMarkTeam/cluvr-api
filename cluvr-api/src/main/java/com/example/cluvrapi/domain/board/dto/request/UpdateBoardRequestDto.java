package com.example.cluvrapi.domain.board.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateBoardRequestDto {
	@NotBlank(message = "제목은 필수입니다.")
	private final String title;

	@NotBlank(message = "내용은 필수입니다.")
	private final String content;

	@Min(value = 10, message = "클로버는 최소 10 이상이어야 합니다.")
	@Max(value = 110, message = "클로버는 최대 110 이하여야 합니다.")
	private final int clover;
}
