package com.example.cluvrapi.domain.reply.repository;

import java.util.List;

import com.example.cluvrapi.domain.reply.dto.response.ReadReplyResponseDto;

public interface ReplyRepositoryCustom {
	List<ReadReplyResponseDto> findAllRepliesByParent(long boardId, Long parentId, int pageNumber,
		int pageSize);
}
