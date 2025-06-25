package com.example.cluvrapi.domain.board.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.example.cluvrapi.domain.board.entity.Board;
import com.example.cluvrapi.domain.category.enums.CategoryType;

@AllArgsConstructor
@Getter
public class ReadBoardResponseDto {
	private long id;
	private String title;
	private String content;
	private CategoryType category;
	private boolean isSelected;
	private int clover;
	private long viewCount;
	private String userName;
	private long like;
	private long dislike;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static ReadBoardResponseDto ofDto(Board board, long viewCount, long likeCount, long dislikeCount) {
		return new ReadBoardResponseDto(
			board.getId(),
			board.getTitle(),
			board.getContent(),
			board.getCategory(),
			board.isSelected(),
			board.getClover(),
			viewCount,
			board.getUser().getName(),
			likeCount,
			dislikeCount,
			board.getCreatedAt(),
			board.getModifiedAt()
		);
	}
}
