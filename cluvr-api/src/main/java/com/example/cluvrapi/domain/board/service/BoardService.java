package com.example.cluvrapi.domain.board.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.board.dto.request.CreateBoardRequestDto;
import com.example.cluvrapi.domain.board.dto.request.UpdateBoardRequestDto;
import com.example.cluvrapi.domain.board.dto.response.ReadBoardResponseDto;
import com.example.cluvrapi.domain.board.dto.response.ReadBoardsResponseDto;
import com.example.cluvrapi.domain.board.enums.ReactionType;
import com.example.cluvrapi.domain.board.dto.response.ReadMyBoardsResponseDto;
import com.example.cluvrapi.domain.category.enums.CategoryType;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;

public interface BoardService {
	/**
	 *
	 * 설명: 게시글 생성 서비스
	 *
	 * @param dto - 생성하는 게시글의 정보
	 * @return 생성된 게시글의 id
	 *
	 * @author yong
	 */
	long createBoard(CreateBoardRequestDto dto);

	/**
	 * 설명: 특정 카테고리에 해당하는 게시글 목록을 페이징 처리하여 조회
	 *
	 * @param category 게시글 카테고리
	 * @param pageNumber 페이지 번호 (1부터 시작)
	 * @param pageSize 페이지당 게시글 수
	 * @return 조회된 게시글 목록
	 * @author yong
	 */
	List<ReadBoardsResponseDto> readBoards(CategoryType category, int pageNumber, int pageSize);

	/**
	 * 설명: 특정 게시글 상세 조회
	 *
	 * @param boardId 조회할 게시글 ID
	 * @return 게시글 상세 정보 DTO
	 * @author yong
	 */
	ReadBoardResponseDto readBoard(long boardId);

	/**
	 * 설명: 특정 게시글을 수정
	 *
	 * @param userId 수정 요청을 보낸 사용자 ID
	 * @param dto 수정할 게시글 정보
	 * @param boardId 수정 대상 게시글 ID
	 * @author yong
	 */
	void updateBoard(long userId, UpdateBoardRequestDto dto, long boardId);

	/**
	 * 설명: 특정 게시글 삭제
	 *
	 * @param boardId 삭제할 게시글 ID
	 * @author yong
	 */
	void deleteBoard(long boardId);

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

  /**
	 * 설명: 자신이 작성했던 게시글 목록 확인
	 *
	 * @param userId - 본인
	 * @param pageable - 페이징
	 * @return
	 *
	 * @author yong
	 */
	PageResponseDto<ReadMyBoardsResponseDto> readBoardsWithUser(long userId, Pageable pageable);
}

