package com.example.cluvrapi.domain.reply.controller;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cluvrapi.domain.reply.dto.request.CreateReplyRequestDto;
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

}
