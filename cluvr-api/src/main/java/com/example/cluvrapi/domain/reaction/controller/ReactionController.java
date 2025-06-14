package com.example.cluvrapi.domain.reaction.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cluvrapi.domain.common.annotation.Auth;
import com.example.cluvrapi.domain.common.dto.AuthUser;
import com.example.cluvrapi.domain.reaction.dto.ReactionRequestDto;
import com.example.cluvrapi.domain.reaction.service.ReactionService;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reaction")
public class ReactionController {

	private final ReactionService reactionService;

	@PostMapping
	public ResponseEntity<BaseResponse<Void>> selectLike(@Auth AuthUser user, @RequestBody ReactionRequestDto dto) {
		reactionService.selectReaction(user.id(), dto);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.NO_CONTENT));
	}

	@DeleteMapping
	public ResponseEntity<BaseResponse<Void>> cancelLike(@Auth AuthUser user, @RequestBody ReactionRequestDto dto) {
		reactionService.cancelReaction(user.id(), dto);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.NO_CONTENT));
	}
}
