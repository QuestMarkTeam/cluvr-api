package com.example.cluvrapi.domain.board.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cluvrapi.domain.board.enums.ReactionType;
import com.example.cluvrapi.domain.board.service.BoardService;
import com.example.cluvrapi.domain.common.annotation.Auth;
import com.example.cluvrapi.domain.common.dto.AuthUser;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class BoardReactonController {
	private final BoardService boardService;

	@PostMapping("/like")
	public ResponseEntity<BaseResponse<Void>> selectLike(@Auth AuthUser user, @PathVariable long boardId,
		@PathVariable long replyId) {
		ReactionType type = ReactionType.LIKE;
		boardService.selectReaction(user.id(), boardId, replyId, type);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.NO_CONTENT));
	}

	@DeleteMapping("/like")
	public ResponseEntity<BaseResponse<Void>> cancelLike(@Auth AuthUser user, @PathVariable long boardId,
		@PathVariable long replyId) {
		ReactionType type = ReactionType.LIKE;
		boardService.cancelReaction(user.id(), boardId, replyId, type);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.NO_CONTENT));
	}

	@PostMapping("/dislike")
	public ResponseEntity<BaseResponse<Void>> selectDislike(@Auth AuthUser user, @PathVariable long boardId,
		@PathVariable long replyId) {
		ReactionType type = ReactionType.DISLIKE;
		boardService.selectReaction(user.id(), boardId, replyId, type);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.NO_CONTENT));
	}

	@DeleteMapping("/dislike")
	public ResponseEntity<BaseResponse<Void>> cancelDisLike(@Auth AuthUser user, @PathVariable long boardId,
		@PathVariable long replyId) {
		ReactionType type = ReactionType.DISLIKE;
		boardService.cancelReaction(user.id(), boardId, replyId, type);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.NO_CONTENT));
	}
}
