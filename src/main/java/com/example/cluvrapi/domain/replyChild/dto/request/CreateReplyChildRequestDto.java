package com.example.cluvrapi.domain.replyChild.dto.request;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateReplyChildRequestDto {
	@NotBlank(message = "내용은 필수입니다.")
	private String content;

	// mentionId는 optional이므로 검증 X
	// 없을 경우 null로 처리
	private Long mentionId;
}
