package com.example.cluvrapi.domain.replyChild.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;

import com.example.cluvrapi.domain.replyChild.entity.MentionInfo;
import com.querydsl.core.annotations.QueryProjection;

@Getter
public class ReadReplyChildrenResponseDto {
	private Long id;
	private String userName;
	private String content;
	private MentionInfo mentionInfo;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	@QueryProjection
	public ReadReplyChildrenResponseDto(Long id, String userName, String content, MentionInfo mentionInfo, LocalDateTime createdAt,
		LocalDateTime updatedAt) {
		this.id = id;
		this.userName = userName;
		this.content = content;
		this.mentionInfo = mentionInfo;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
}
