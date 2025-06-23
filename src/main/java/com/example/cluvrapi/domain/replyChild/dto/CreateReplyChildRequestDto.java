package com.example.cluvrapi.domain.replyChild.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateReplyChildRequestDto {
	private String content;
	private Long mentionId;
}
