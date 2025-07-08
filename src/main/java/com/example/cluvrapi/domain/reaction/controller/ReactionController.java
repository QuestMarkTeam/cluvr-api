package com.example.cluvrapi.domain.reaction.controller;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cluvrapi.domain.common.annotation.Auth;
import com.example.cluvrapi.domain.common.dto.AuthUser;
import com.example.cluvrapi.domain.reaction.dto.request.ReactionRequestDto;
import com.example.cluvrapi.domain.reaction.service.ReactionService;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reactions")
public class ReactionController {

	private final ReactionService reactionService;

	/**
	 *
	 * 설명: 좋아요 혹은 싫어요를 선택하는 기능
	 *
	 * @param user - 좋아요 혹은 싫어요를 누르는 유저 정보
	 * @param dto - 리액션 타입(좋아요, 싫어요), 보드 정보, 댓글 정보(안 써도 됨)
	 *
	 * @author yong
	 */
	@PostMapping
	public ResponseEntity<BaseResponse<Void>> selectLike(@Auth AuthUser user,
		@Valid @RequestBody ReactionRequestDto dto) {
		reactionService.selectReaction(user.id(), dto);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.NO_CONTENT));
	}

	/**
	 *
	 * 설명: 선택되었던 좋아요 혹은 싫어요를 선택 취소하는 기능
	 *
	 * @param user - 좋아요 혹은 싫어요를 누르는 유저 정보
	 * @param dto - 리액션 타입(좋아요, 싫어요), 보드 정보, 댓글 정보(안 써도 됨)
	 *
	 * @author yong
	 */
	@DeleteMapping
	public ResponseEntity<BaseResponse<Void>> cancelLike(@Auth AuthUser user,
		@Valid @RequestBody ReactionRequestDto dto) {
		reactionService.cancelReaction(user.id(), dto);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.NO_CONTENT));
	}

	/**
	 * Redis 리액션 카운트 초기화 (개발용)
	 * @param boardId 게시글 ID
	 * @return 초기화 완료 응답
	 */
	@PostMapping("/reset/{boardId}")
	public ResponseEntity<BaseResponse<Void>> resetReactionCount(@PathVariable Long boardId) {
		reactionService.resetBoardReactionCount(boardId);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.NO_CONTENT));
	}
}
