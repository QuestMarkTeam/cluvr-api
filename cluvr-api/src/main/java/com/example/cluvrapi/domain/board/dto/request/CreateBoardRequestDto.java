package com.example.cluvrapi.domain.board.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.example.cluvrapi.domain.board.entity.Board;
import com.example.cluvrapi.domain.category.enums.CategoryType;

@Getter
@AllArgsConstructor
public class CreateBoardRequestDto {
	@NotBlank(message = "제목은 필수입니다.")
	private String title;

	@NotBlank(message = "내용은 필수입니다.")
	private String content;

	@NotNull(message = "카테고리는 필수입니다.")
	private CategoryType category;

	@Min(value = 10, message = "클로버는 최소 10 이상이어야 합니다.")
	@Max(value = 110, message = "클로버는 최대 110 이하여야 합니다.")
	private int clover;

	public Board fromDto() {
		return new Board(
			category,
			title,
			content,
			0,       // 댓글 수
			false,   // 삭제 여부
			clover
		);
	}
}
