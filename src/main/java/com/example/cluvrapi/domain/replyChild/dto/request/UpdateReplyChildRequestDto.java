package com.example.cluvrapi.domain.replyChild.dto.request;

import java.util.Optional;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateReplyChildRequestDto {
	@NotBlank(message = "댓글 내용을 입력해주세요.")
	private final String content;

	// 언급을 유지하거나, 제거하거나, 변경할 수 있음
	private final Optional<Long> mentionedUserId;
}
