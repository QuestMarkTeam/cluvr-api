package com.example.cluvrapi.domain.reply.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;

import com.example.cluvrapi.domain.reply.entity.Reply;
import com.querydsl.core.annotations.QueryProjection;

@Getter
public class ReadReplyResponseDto {
	private long id;
	private String content;
	private String userName;
	private long like;
	private long dislike;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public ReadReplyResponseDto(Reply reply, long like, long dislike) {
		this.id = reply.getId();
		this.content = reply.getContent();
		this.userName = reply.getUser().getName();
		this.like = like;
		this.dislike = dislike;
		this.createdAt = reply.getCreatedAt();
		this.updatedAt = reply.getModifiedAt();
	}
}
