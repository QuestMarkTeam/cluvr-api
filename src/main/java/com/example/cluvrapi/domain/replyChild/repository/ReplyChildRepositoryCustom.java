package com.example.cluvrapi.domain.replyChild.repository;

import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.replyChild.dto.response.ReadReplyChildrenResponseDto;

public interface ReplyChildRepositoryCustom {
	PageResponseDto<ReadReplyChildrenResponseDto> findAllReplyChildrenByParent(long replyId, Pageable pageable);
}
