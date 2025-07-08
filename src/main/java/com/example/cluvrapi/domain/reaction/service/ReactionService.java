package com.example.cluvrapi.domain.reaction.service;

import com.example.cluvrapi.domain.reaction.dto.request.ReactionRequestDto;

public interface ReactionService {
	/**
	 *
	 * 설명: 좋아요 혹은 싫어요 선택을 위한 서비스 파트
	 *
	 * @param userId - Auth 유저
	 * @param dto - board, reply, reaction 타입
	 *
	 * @author yong
	 */
	void selectReaction(long userId, ReactionRequestDto dto);

	/**
	 *
	 * 설명: 좋아요 혹은 싫어요 선택 취소를 위한 서비스 파트
	 *
	 * @param userId - Auth 유저
	 * @param dto - board, reply, reaction 타입
	 *
	 * @author yong
	 */
	void cancelReaction(long userId, ReactionRequestDto dto);

	/**
	 * Redis 리액션 카운트 초기화 (개발용)
	 * @param boardId 게시글 ID
	 */
	void resetBoardReactionCount(Long boardId);
}
