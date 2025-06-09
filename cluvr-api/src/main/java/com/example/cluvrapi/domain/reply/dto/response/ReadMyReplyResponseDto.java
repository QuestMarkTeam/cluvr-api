package com.example.cluvrapi.domain.reply.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;

import com.querydsl.core.annotations.QueryProjection;

@Getter
public class ReadMyReplyResponseDto {
	private long id;
	private String content;
	private LocalDateTime createdAt;

	@QueryProjection
	public ReadMyReplyResponseDto(long id, String content, LocalDateTime createdAt) {
		this.id = id;
		this.content = content;
		this.createdAt = createdAt;
	}
}
