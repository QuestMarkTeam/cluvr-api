package com.example.cluvrapi.domain.reaction.service;

import com.example.cluvrapi.domain.reaction.dto.request.ReactionRequestDto;
import com.example.cluvrapi.domain.reaction.dto.response.ReactionStatusResponseDto;

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

	/**
	 * 사용자의 리액션 상태 조회
	 * @param userId 사용자 ID
	 * @param boardId 게시글 ID
	 * @param replyId 댓글 ID (선택사항)
	 * @return 사용자의 리액션 상태
	 */
	ReactionStatusResponseDto getReactionStatus(Long userId, Long boardId, Long replyId);
}
