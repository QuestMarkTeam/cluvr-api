package com.example.cluvrapi.domain.replyChild.service;

import com.example.cluvrapi.domain.replyChild.dto.CreateReplyChildRequestDto;

public interface ReplyChildService {
	long createReply(long userId, long boardId, CreateReplyChildRequestDto dto);
}
