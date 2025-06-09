package com.example.cluvrapi.domain.til.dto.response;

import lombok.Getter;

import com.querydsl.core.annotations.QueryProjection;

@Getter
public class InfoTilResponseDto {
	private Long authorId;
	private Long userId;
	private String title;
	private String content;

	@QueryProjection
	public InfoTilResponseDto(Long authorId, Long userId, String title, String content) {
		this.authorId = authorId;
		this.userId = userId;
		this.title = title;
		this.content = content;
	}
}
