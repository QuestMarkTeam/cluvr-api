package com.example.cluvrapi.domain.reply.repository;

import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.category.enums.CategoryType;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.reply.dto.response.ReadMyReplyResponseDto;
import com.example.cluvrapi.domain.reply.dto.response.ReadReplyResponseDto;

public interface ReplyRepositoryCustom {
	PageResponseDto<ReadReplyResponseDto> findAllRepliesByBoard(long boardId, Pageable pageable);

	PageResponseDto<ReadMyReplyResponseDto> findRepliesByUser(long userId, Pageable pageable);

	Map<CategoryType, Long> countRepliesPerCategoryByUser(long userId);
}
