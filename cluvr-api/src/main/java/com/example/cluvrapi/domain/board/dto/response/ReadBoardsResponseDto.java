package com.example.cluvrapi.domain.board.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReadBoardsResponseDto {
	private long id;
	private String title;
	private String content;
	private int view;
	// private String userName;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

}
