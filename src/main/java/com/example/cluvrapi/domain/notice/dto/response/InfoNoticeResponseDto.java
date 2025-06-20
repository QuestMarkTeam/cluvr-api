package com.example.cluvrapi.domain.notice.dto.response;

import lombok.Getter;

import com.querydsl.core.annotations.QueryProjection;

@Getter
public class InfoNoticeResponseDto {
	private Long noticeId;
	private Long authorId;
	private String title;
	private String contents;

	@QueryProjection
	public InfoNoticeResponseDto(Long noticeId, Long authorId, String title, String contents) {
		this.noticeId = noticeId;
		this.authorId = authorId;
		this.title = title;
		this.contents = contents;
	}
}
