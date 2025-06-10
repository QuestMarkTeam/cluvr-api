package com.example.cluvrapi.domain.reply.service;

import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.board.enums.ReactionType;
import com.example.cluvrapi.domain.category.enums.CategoryType;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.reply.dto.request.CreateReplyRequestDto;
import com.example.cluvrapi.domain.reply.dto.request.UpdateReplyRequestDto;
import com.example.cluvrapi.domain.reply.dto.response.ReadMyReplyResponseDto;
import com.example.cluvrapi.domain.reply.dto.response.ReadReplyResponseDto;

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

	/**
	 *
	 * 설명: 댓글 조회
	 *
	 * <p> parentId를 기준으로 댓글을 조회합니다.</p>
	 * <p> parentId가 null 일 시, 게시글의 댓글입니다.</p>
	 * <p> parentId가 null이 아닐 시, 대댓글입니다.</p>
	 *
	 * @param boardId - 댓글이 소속되어 있는 게시글 id
	 * @param parentId - null: 댓글 / null 아닐 시: 대댓글
	 * @param pageable - 페이지 정보
	 * @return 페이지 수 만큼 댓글을 조회합니다.
	 *
	 * @author yong
	 */
	PageResponseDto<ReadReplyResponseDto> readReplies(long boardId, Long parentId, Pageable pageable);

	/**
	 *
	 * 설명: 작성했던 댓글 수정
	 *
	 * @param userId - 업데이트 전 생성자가 맞는지 확인
	 * @param boardId - 필요한가?
	 * @param replyId - 업데이트 하려는 댓글 id
	 * @param dto - 업데이트하고 싶은 데이터
	 * @author yong
	 */
	void updateReply(long userId, long boardId, long replyId, UpdateReplyRequestDto dto);

	/**
	 *
	 * 설명: 삭제할 댓글(소프트 딜리트)
	 *
	 * @param userId - 업데이트 전 생성자가 맞는지 확인
	 * @param boardId - 필요한가?
	 * @param replyId - 삭제하려는 댓글 id
	 * @author yong
	 */
	void deleteReply(long userId, long boardId, long replyId);

	/**
	 *
	 * 설명: 좋아요 혹은 싫어요 선택
	 *
	 * @param userId - Auth 유저
	 * @param boardId - 댓글 게시글
	 * @param replyId - 댓글
	 * @param reaction - 반응 타입(좋아요 혹은 싫어요)
	 *
	 * @author yong
	 */
	void selectReaction(long userId, long boardId, long replyId, ReactionType reaction);

	/**
	 *
	 * 설명: 좋아요 혹은 싫어요 선택 취소
	 *
	 * @param userId - Auth 유저
	 * @param boardId - 댓글 게시글
	 * @param replyId - 댓글
	 * @param reaction - 반응 타입(좋아요 혹은 싫어요)
	 *
	 * @author yong
	 */
	void cancelReaction(long userId, long boardId, long replyId, ReactionType reaction);

	PageResponseDto<ReadMyReplyResponseDto> readRepliesWithUser(long userId, Pageable pageable);

	Map<CategoryType, Long> readReplyCountPerCategoryByUser(long userId);
}
