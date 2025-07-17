package com.example.cluvrapi.domain.board.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.board.dto.request.CreateBoardRequestDto;
import com.example.cluvrapi.domain.board.dto.request.UpdateBoardRequestDto;
import com.example.cluvrapi.domain.board.dto.response.ReadBoardResponseDto;
import com.example.cluvrapi.domain.board.dto.response.ReadAllBoardsResponseDto;
import com.example.cluvrapi.domain.board.dto.response.ReadMyBoardsResponseDto;
import com.example.cluvrapi.domain.board.enums.BoardType;
import com.example.cluvrapi.domain.category.enums.CategoryType;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;

public interface BoardService {
	/**
	 *
	 * 설명: 게시글 생성 서비스
	 *
	 * @param userId - 생성자
	 * @param dto - 생성하는 게시글의 정보
	 * @return 생성된 게시글의 id
	 *
	 * @author yong
	 */
	long createBoard(long userId, CreateBoardRequestDto dto);

	/**
	 * 설명: 특정 카테고리에 해당하는 게시글 목록을 페이징 처리하여 조회
	 *
	 * @param category - 게시글 카테고리
	 * @param pageable - 페이지 번호, 페이지당 게시글 수
	 * @param boardType - 보드 타입
	 * @return 조회된 게시글 목록
	 * @author yong
	 */
	// PageResponseDto<ReadAllBoardsResponseDto> readBoards(CategoryType category, BoardType boardType, Pageable pageable);

	/**
	 * 설명: 특정 게시글 상세 조회
	 *
	 * @param boardId 조회할 게시글 ID
	 * @return 게시글 상세 정보 DTO
	 * @author yong
	 */
	ReadBoardResponseDto readBoard(long boardId, long userId);

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
	 * @param userId 게시글 만든 유저 ID
	 * @param boardId 삭제할 게시글 ID
	 * @author yong
	 */
	void deleteBoard(long userId, long boardId);

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

	/**
	 *
	 * 설명: 게시글의 주인은 댓글 중 하나를 채택합니다.
	 *
	 * @param userId
	 * @param boardId
	 * @param replyId
	 *
	 * @author yong
	 */
	void selectBestReply(long userId, long boardId, long replyId);

	/**
	 *
	 * 설명: 메인 페이지에서 당일 조회수 증가 수와 좋아요 수, 싫어요 수를 사용하여 인기 게시글을 가져온다.
	 *
	 * @param categoryType
	 * @return
	 *
	 * @author yong
	 */
	List<ReadAllBoardsResponseDto> readRecommendedBoards(CategoryType categoryType);
}

