package com.example.cluvrapi.domain.board.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;

import com.example.cluvrapi.domain.board.entity.Board;
import com.querydsl.core.annotations.QueryProjection;

@Getter
public class ReadAllBoardsResponseDto {
	private long id;
	private String title;
	private String content;
	private long viewCount;
	private String userName;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	@QueryProjection
	public ReadAllBoardsResponseDto(Board board) {
		this.id = board.getId();
		this.title = board.getTitle();
		this.content = board.getContent();
		this.viewCount = 0;
		this.userName = board.getUser().getName();
		this.createdAt = board.getCreatedAt();
		this.updatedAt = board.getModifiedAt();
	}
}
