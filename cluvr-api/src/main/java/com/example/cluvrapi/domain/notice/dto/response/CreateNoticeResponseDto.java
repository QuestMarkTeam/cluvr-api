package com.example.cluvrapi.domain.notice.dto.response;

import lombok.Getter;

@Getter
public class CreateNoticeResponseDto {

	private Long noticeId;

	public CreateNoticeResponseDto(Long noticeId) {
		this.noticeId = noticeId;
	}

	public static CreateNoticeResponseDto from(Long noticeId) {
		return new CreateNoticeResponseDto(noticeId);
	}
}
