package com.example.cluvrapi.domain.replyChild.service;

import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.replyChild.dto.CreateReplyChildRequestDto;
import com.example.cluvrapi.domain.replyChild.dto.response.ReadReplyChildrenResponseDto;

public interface ReplyChildService {
	long createReply(long userId, long boardId, CreateReplyChildRequestDto dto);

	PageResponseDto<ReadReplyChildrenResponseDto> readReplychildren(long replyId, Pageable pageable);
}
