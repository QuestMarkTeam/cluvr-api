package com.example.cluvrapi.domain.reply.controller;

import java.util.List;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cluvrapi.domain.reply.dto.request.CreateReplyRequestDto;
import com.example.cluvrapi.domain.reply.dto.request.UpdateReplyRequestDto;
import com.example.cluvrapi.domain.reply.dto.response.ReadReplyResponseDto;
import com.example.cluvrapi.domain.reply.service.ReplyService;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

@RestController
@RequestMapping("/boards/{boardId}/replies")
@RequiredArgsConstructor
@Validated
public class ReplyController {
	private final ReplyService replyService;

	@PostMapping
	public ResponseEntity<BaseResponse<Long>> createReply(@PathVariable long boardId,
		@Valid @RequestBody CreateReplyRequestDto dto) {
		long userId = 1;
		return ResponseEntity.ok(
			BaseResponse.success(replyService.createReply(userId, boardId, dto), ResponseCode.CREATED));
	}

	@GetMapping
	public ResponseEntity<BaseResponse<List<ReadReplyResponseDto>>> readRepliesWithParent(
		@PathVariable long boardId,
		@RequestParam(required = false) Long parentId,
		@RequestParam(defaultValue = "1") int pageNumber,
		@RequestParam(defaultValue = "10") int pageSize
	) {
		return ResponseEntity.ok(
			BaseResponse.success(replyService.readReplies(boardId, parentId, pageNumber, pageSize),
				ResponseCode.OK));
	}

	@PatchMapping("/{replyId}")
	public ResponseEntity<BaseResponse<List<Void>>> updateReply(
		@PathVariable long boardId,
		@PathVariable long replyId,
		@RequestBody UpdateReplyRequestDto dto
	) {
		long userId = 1L;
		return ResponseEntity.ok(
			BaseResponse.success(replyService.updateReply(userId, boardId, replyId, dto),
				ResponseCode.OK));
	}
}
