package com.example.cluvrapi.domain.board.service;

import com.example.cluvrapi.domain.board.dto.request.CreateBoardRequestDto;

public interface BoardService {
	long createBoard(CreateBoardRequestDto dto);
}

