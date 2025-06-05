package com.example.cluvrapi.domain.reply.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;

import com.querydsl.core.annotations.QueryProjection;

@Getter
public class ReadReplyResponseDto {
	private String content;

	private String userName;
	// private int like;
	// private int dislike;

	private LocalDateTime createdAt;

	@QueryProjection
	public ReadReplyResponseDto(String content, String userName, LocalDateTime createdAt) {
		this.content = content;
		this.userName = userName;
		this.createdAt = createdAt;
	}
}
