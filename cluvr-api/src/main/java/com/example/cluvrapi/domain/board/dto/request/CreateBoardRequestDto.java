package com.example.cluvrapi.domain.board.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.example.cluvrapi.domain.board.entity.Board;
import com.example.cluvrapi.domain.category.enums.CategoryType;

@Getter
@AllArgsConstructor
public class CreateBoardRequestDto {
	private final String title;
	private final String content;
	private final CategoryType category;
	private final int clover;

	public Board fromDto() {
		return new Board(
			category,
			title,
			content,
			0,
			false,
			clover
		);
	}
}
