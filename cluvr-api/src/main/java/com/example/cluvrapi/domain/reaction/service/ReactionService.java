package com.example.cluvrapi.domain.reaction.service;

import com.example.cluvrapi.domain.reaction.dto.ReactionRequestDto;

public interface ReactionService {
	/**
	 *
	 * 설명: 좋아요 혹은 싫어요 선택
	 *
	 * @param userId - Auth 유저
	 * @param dto - board, reply, reaction 타입
	 *
	 * @author yong
	 */
	void selectReaction(long userId, ReactionRequestDto dto);

	/**
	 *
	 * 설명: 좋아요 혹은 싫어요 선택 취소
	 *
	 * @param userId - Auth 유저
	 * @param dto - board, reply, reaction 타입
	 *
	 * @author yong
	 */
	void cancelReaction(long userId, ReactionRequestDto dto);
}
