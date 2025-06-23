package com.example.cluvrapi.domain.replyChild.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateReplyChildRequestDto {
	final String content;
	final Long mentionedUserId;
}
