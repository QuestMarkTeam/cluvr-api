package com.example.cluvrapi.domain.reply.service;

import java.util.List;

import com.example.cluvrapi.domain.reply.dto.request.CreateReplyRequestDto;
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
}
