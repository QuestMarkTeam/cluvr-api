package com.example.cluvrapi.domain.board.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
	private int view;
	// private String userName;
	// private int like;
	// private int dislike;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
