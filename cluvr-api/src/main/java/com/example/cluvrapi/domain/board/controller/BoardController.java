package com.example.cluvrapi.domain.board.controller;

import java.util.List;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import com.example.cluvrapi.domain.board.dto.response.ReadMyBoardsResponseDto;
import com.example.cluvrapi.domain.board.service.BoardService;
import com.example.cluvrapi.domain.category.enums.CategoryType;
import com.example.cluvrapi.domain.common.annotation.Auth;
import com.example.cluvrapi.domain.common.dto.AuthUser;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
@Validated
public class BoardController {

	private final BoardService boardService;

	@PostMapping
	public ResponseEntity<BaseResponse<Long>> createBoard(@Valid @RequestBody CreateBoardRequestDto dto) {
		return ResponseEntity.ok(BaseResponse.success(boardService.createBoard(dto), ResponseCode.CREATED));
	}

	@GetMapping
	public ResponseEntity<BaseResponse<List<ReadBoardsResponseDto>>> readBoards(@RequestParam CategoryType category,
		@RequestParam int pageNumber, @RequestParam int pageSize) {
		return ResponseEntity.ok(
			BaseResponse.success(boardService.readBoards(category, pageNumber, pageSize), ResponseCode.OK));
	}

	@GetMapping("/{boardId}")
	public ResponseEntity<BaseResponse<ReadBoardResponseDto>> readBoard(@PathVariable long boardId) {
		return ResponseEntity.ok(BaseResponse.success(boardService.readBoard(boardId), ResponseCode.OK));
	}

	@PatchMapping("/{boardId}")
	public ResponseEntity<BaseResponse<Void>> updateBoard(@Valid @RequestBody UpdateBoardRequestDto dto,
		@PathVariable long boardId) {
		long userId = 1;
		boardService.updateBoard(userId, dto, boardId);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.OK));
	}

	@DeleteMapping("/{boardId}")
	public ResponseEntity<BaseResponse<Void>> deleteBoard(@PathVariable long boardId) {
		boardService.deleteBoard(boardId);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.OK));
	}

	@GetMapping("/me")
	public ResponseEntity<BaseResponse<PageResponseDto<ReadMyBoardsResponseDto>>> readBoardsWithUser(
		@Auth AuthUser user, @PageableDefault(size = 5, sort = "createdAt") Pageable pageable) {

		long id = 1;
		return ResponseEntity.ok(
			BaseResponse.success(boardService.readBoardsWithUser(id, pageable), ResponseCode.OK));
	}
}
