package com.example.cluvrapi.domain.board.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;

import com.querydsl.core.annotations.QueryProjection;

@Getter
public class ReadBoardsResponseDto {
	private long id;
	private String title;
	private String content;
	private int view;
	private String userName;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	@QueryProjection
	public ReadBoardsResponseDto(long id, String title, String content, int view, String userName,
		LocalDateTime createdAt,
		LocalDateTime updatedAt) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.view = view;
		this.userName = userName;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
}
