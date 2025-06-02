package com.example.cluvrapi.domain.board.service;

import org.springframework.data.domain.Page;

import com.example.cluvrapi.domain.board.dto.request.CreateBoardRequestDto;
import com.example.cluvrapi.domain.board.dto.response.ReadBoardResponseDto;
import com.example.cluvrapi.domain.board.dto.response.ReadBoardsResponseDto;
import com.example.cluvrapi.domain.category.enums.CategoryType;

public interface BoardService {
	long createBoard(CreateBoardRequestDto dto);

	Page<ReadBoardsResponseDto> readBoards(CategoryType category, int pageNumber, int pageSize);

	ReadBoardResponseDto readBoard(long boardId);
}

