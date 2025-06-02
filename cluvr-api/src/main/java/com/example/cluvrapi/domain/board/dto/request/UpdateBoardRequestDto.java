package com.example.cluvrapi.domain.board.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateBoardRequestDto {
	private final String title;
	private final String content;
	private final int clover;
}
