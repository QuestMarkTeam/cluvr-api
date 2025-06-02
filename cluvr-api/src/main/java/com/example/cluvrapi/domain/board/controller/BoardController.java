package com.example.cluvrapi.domain.board.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cluvrapi.domain.board.dto.request.CreateBoardRequestDto;
import com.example.cluvrapi.domain.board.dto.request.UpdateBoardRequestDto;
import com.example.cluvrapi.domain.board.dto.response.ReadBoardResponseDto;
import com.example.cluvrapi.domain.board.dto.response.ReadBoardsResponseDto;
import com.example.cluvrapi.domain.board.service.BoardService;
import com.example.cluvrapi.domain.category.enums.CategoryType;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

	private final BoardService boardService;

	@PostMapping
	public long createBoard(@RequestBody CreateBoardRequestDto dto) {
		return boardService.createBoard(dto);
	}

	@GetMapping
	public Page<ReadBoardsResponseDto> readBoards(@RequestParam CategoryType category, @RequestParam int pageNumber,
		@RequestParam int pageSize) {
		return boardService.readBoards(category, pageNumber, pageSize);
	}

	@GetMapping("/{boardId}")
	public ReadBoardResponseDto readBoard(@PathVariable long boardId) {
		return boardService.readBoard(boardId);
	}

	@PatchMapping("/{boardId}")
	public void updateBoard(@RequestBody UpdateBoardRequestDto dto, @PathVariable long boardId) {
		boardService.updateBoard(dto, boardId);
	}

	@DeleteMapping("/{boardId}")
	public void deleteBoard(@PathVariable long boardId) {
		boardService.deleteBoard(boardId);
	}
}
