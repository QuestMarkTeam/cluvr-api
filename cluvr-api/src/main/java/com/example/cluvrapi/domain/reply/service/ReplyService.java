package com.example.cluvrapi.domain.reply.service;

import com.example.cluvrapi.domain.reply.dto.request.CreateReplyRequestDto;

public interface ReplyService {
	/**
	 *
	 * 설명: 댓글 생성
	 *
	 * @param userId - 댓글 생성자의 id
	 * @param boardId - 댓글이 소속된 게시글 id
	 * @param dto - 댓글 정보
	 * @return 생성된 댓글의 id
	 *
	 * @author yong
	 */
	Long createReply(long userId, long boardId, CreateReplyRequestDto dto);
}
