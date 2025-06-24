package com.example.cluvrapi.domain.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.hibernate.validator.constraints.Range;

import com.example.cluvrapi.domain.board.entity.Board;
import com.example.cluvrapi.domain.board.enums.BoardType;
import com.example.cluvrapi.domain.category.enums.CategoryType;
import com.example.cluvrapi.domain.clover.dto.CloverEarnDto;
import com.example.cluvrapi.domain.user.entity.User;

@Getter
@AllArgsConstructor
public class CreateBoardRequestDto implements CloverEarnDto {
	@NotBlank(message = "제목은 필수입니다.")
	private String title;

	@NotBlank(message = "내용은 필수입니다.")
	private String content;

	@NotNull(message = "게시글의 종류는 필수입니다.")
	private BoardType boardType;

	@NotNull(message = "카테고리는 필수입니다.")
	private CategoryType category;

	@Range(min = 0, max = 100, message = "게시글 하나에 사용할 수 있는 클로버는 0부터 100 사이 입니다.")
	private Integer clover;

	private final int DEFAULT_BOARD_CLOVER = 10;

	public Board fromDto(User user) {
		return new Board(
			user,
			boardType,
			category,
			title,
			content,
			clover + DEFAULT_BOARD_CLOVER
		);
	}

	@Override
	public Integer getClover() {
		return this.clover;
	}
}
