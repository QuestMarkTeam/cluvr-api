package com.example.cluvrapi.domain.reply.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateReplyRequestDto {
	@NotBlank(message = "댓글 내용이 비어있습니다.")
	@Size(min = 1, max = 255, message = "댓글 내용은 10자에서 255자 사이로 작성하셔야 합니다.")
	private String content;
}
