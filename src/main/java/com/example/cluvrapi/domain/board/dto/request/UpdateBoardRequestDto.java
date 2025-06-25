package com.example.cluvrapi.domain.board.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.hibernate.validator.constraints.Range;

@Getter
@AllArgsConstructor
public class UpdateBoardRequestDto {

	private final String title;

	private final String content;

	@Range(min = 0, max = 100, message = "게시글 하나에 사용할 수 있는 클로버는 0부터 100 사이 입니다.")
	private final Integer clover;
}
