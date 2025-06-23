package com.example.cluvrapi.domain.replyChild.controller;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cluvrapi.domain.common.annotation.Auth;
import com.example.cluvrapi.domain.common.dto.AuthUser;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.reply.dto.request.CreateReplyRequestDto;
import com.example.cluvrapi.domain.reply.dto.response.ReadReplyResponseDto;
import com.example.cluvrapi.domain.replyChild.dto.CreateReplyChildRequestDto;
import com.example.cluvrapi.domain.replyChild.dto.response.ReadReplyChildrenResponseDto;
import com.example.cluvrapi.domain.replyChild.service.ReplyChildService;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

@RestController
@RequiredArgsConstructor
public class ReplyChildController {

	private final ReplyChildService replyChildService;

	/**
	 * 대댓글 생성
	 */
	@PostMapping("/replies/{replyId}/reply-children")
	public ResponseEntity<BaseResponse<Long>> createReplyChild(@Auth AuthUser user, @PathVariable long replyId,
		@Valid @RequestBody CreateReplyChildRequestDto dto) {
		return ResponseEntity.ok(
			BaseResponse.success(replyChildService.createReply(user.id(), replyId, dto), ResponseCode.CREATED));
	}

	/**
	 * a댓글에 해당하는 대댓글 페이징 조회
	 */
	@GetMapping("/replies/{replyId}/reply-children")
	public ResponseEntity<BaseResponse<PageResponseDto<ReadReplyChildrenResponseDto>>> readRepliesWithParent(
		@PathVariable long replyId,
		@PageableDefault(size = 5, sort = "createdAt") Pageable pageable) {
		return ResponseEntity.ok(
			BaseResponse.success(replyChildService.readReplychildren(replyId, pageable), ResponseCode.OK));
	}
}
