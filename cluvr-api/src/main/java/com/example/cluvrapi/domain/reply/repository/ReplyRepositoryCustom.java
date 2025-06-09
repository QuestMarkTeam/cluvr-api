package com.example.cluvrapi.domain.reply.repository;

import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.reply.dto.response.ReadMyReplyResponseDto;
import com.example.cluvrapi.domain.reply.dto.response.ReadReplyResponseDto;

public interface ReplyRepositoryCustom {
	PageResponseDto<ReadReplyResponseDto> findAllRepliesByParent(long boardId, Long parentId, Pageable pageable);

	PageResponseDto<ReadMyReplyResponseDto> findRepliesByUser(long userId, Pageable pageable);
}
