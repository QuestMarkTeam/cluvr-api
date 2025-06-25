package com.example.cluvrapi.domain.board.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;

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
	public ReadAllBoardsResponseDto(long id, String title, String content, long viewCount, String userName,
		LocalDateTime createdAt,
		LocalDateTime updatedAt) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.viewCount = viewCount;
		this.userName = userName;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
}
