package com.example.cluvrapi.domain.board.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;

import com.querydsl.core.annotations.QueryProjection;

@Getter
public class ReadMyBoardsResponseDto {
	private long id;
	private String title;
	private String content;
	private LocalDateTime createdAt;

	@QueryProjection
	public ReadMyBoardsResponseDto(long id, String title, String content, LocalDateTime createdAt) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.createdAt = createdAt;
	}
}
