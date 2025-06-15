package com.example.cluvrapi.domain.board.controller;

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
	public ResponseEntity<BaseResponse<Long>> createBoard(@Auth AuthUser user,
		@Valid @RequestBody CreateBoardRequestDto dto) {
		return ResponseEntity.ok(BaseResponse.success(boardService.createBoard(user.id(), dto), ResponseCode.CREATED));
	}

	@GetMapping
	public ResponseEntity<BaseResponse<PageResponseDto<ReadBoardsResponseDto>>> readBoards(
		@RequestParam CategoryType category,
		@PageableDefault(size = 5, sort = "createdAt") Pageable pageable) {
		return ResponseEntity.ok(
			BaseResponse.success(boardService.readBoards(category, pageable), ResponseCode.OK));
	}

	@GetMapping("/{boardId}")
	public ResponseEntity<BaseResponse<ReadBoardResponseDto>> readBoard(@PathVariable long boardId) {
		return ResponseEntity.ok(BaseResponse.success(boardService.readBoard(boardId), ResponseCode.OK));
	}

	@PatchMapping("/{boardId}")
	public ResponseEntity<BaseResponse<Void>> updateBoard(@Auth AuthUser user,
		@Valid @RequestBody UpdateBoardRequestDto dto,
		@PathVariable long boardId) {
		boardService.updateBoard(user.id(), dto, boardId);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.OK));
	}

	@DeleteMapping("/{boardId}")
	public ResponseEntity<BaseResponse<Void>> deleteBoard(@Auth AuthUser user, @PathVariable long boardId) {
		boardService.deleteBoard(user.id(), boardId);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.OK));
	}

	@GetMapping("/me")
	public ResponseEntity<BaseResponse<PageResponseDto<ReadMyBoardsResponseDto>>> readBoardsWithUser(
		@Auth AuthUser user, @PageableDefault(size = 5, sort = "createdAt") Pageable pageable) {
		return ResponseEntity.ok(
			BaseResponse.success(boardService.readBoardsWithUser(user.id(), pageable), ResponseCode.OK));
	}

	@PostMapping("/{boardId}/replies/{replyId}/best-recommendation")
	public ResponseEntity<BaseResponse<Void>> selectBestReply(@Auth AuthUser user, @PathVariable long boardId,
		@PathVariable long replyId) {
		boardService.selectBestReply(user.id(), boardId, replyId);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.OK));
	}
}
