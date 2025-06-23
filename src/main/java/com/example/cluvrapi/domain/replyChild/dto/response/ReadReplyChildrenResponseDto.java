package com.example.cluvrapi.domain.replyChild.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;

import com.example.cluvrapi.domain.replyChild.entity.MentionInfo;
import com.querydsl.core.annotations.QueryProjection;

@Getter
public class ReadReplyChildrenResponseDto {
	private Long id;
	private String userName;
	private MentionInfo mentionInfo;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	@QueryProjection
	public ReadReplyChildrenResponseDto(Long id, String userName, MentionInfo mentionInfo, LocalDateTime createdAt,
		LocalDateTime updatedAt) {
		this.id = id;
		this.userName = userName;
		this.mentionInfo = mentionInfo;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
}
