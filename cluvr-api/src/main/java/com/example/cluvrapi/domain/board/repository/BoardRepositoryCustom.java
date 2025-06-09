package com.example.cluvrapi.domain.board.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.board.dto.response.ReadBoardsResponseDto;
import com.example.cluvrapi.domain.board.dto.response.ReadMyBoardsResponseDto;
import com.example.cluvrapi.domain.board.entity.Board;
import com.example.cluvrapi.domain.category.enums.CategoryType;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;

public interface BoardRepositoryCustom {
	/**
	 *
	 * 설명: 게시판 상세 정보입니다.
	 *
	 * @param id
	 * @return 게시판의 상세 정보
	 *
	 * @author yong
	 */
	Board findBoardById(long id);

	/**
	 *
	 * 설명: 게시판 목록을 위한 정보입니다.
	 *
	 * @param category
	 * @param pageNumber
	 * @param pageSize
	 * @return 카테고리에 해당하는 게시판 리스트
	 *
	 * @author yong
	 */
	List<ReadBoardsResponseDto> findAllBoardsByCategory(CategoryType category, int pageNumber, int pageSize);

	PageResponseDto<ReadMyBoardsResponseDto> findBoardsByUser(long userId, Pageable pageable);
}
