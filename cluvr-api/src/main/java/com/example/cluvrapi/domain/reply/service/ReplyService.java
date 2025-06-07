package com.example.cluvrapi.domain.reply.service;

import java.util.List;

import com.example.cluvrapi.domain.reply.dto.request.CreateReplyRequestDto;
import com.example.cluvrapi.domain.reply.dto.request.UpdateReplyRequestDto;
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
	 * @param pageNumber - 페이지 번호
	 * @param pageSize - 가져올 댓글 갯수
	 * @return 페이지 수 만큼 댓글을 조회합니다.
	 *
	 * @author yong
	 */
	List<ReadReplyResponseDto> readReplies(long boardId, Long parentId, int pageNumber, int pageSize);

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
}
