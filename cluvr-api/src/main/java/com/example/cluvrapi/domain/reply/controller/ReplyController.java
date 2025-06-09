package com.example.cluvrapi.domain.reply.controller;

import java.util.List;
import java.util.Map;

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

import com.example.cluvrapi.domain.category.enums.CategoryType;
import com.example.cluvrapi.domain.common.annotation.Auth;
import com.example.cluvrapi.domain.common.dto.AuthUser;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.reply.dto.request.CreateReplyRequestDto;
import com.example.cluvrapi.domain.reply.dto.request.UpdateReplyRequestDto;
import com.example.cluvrapi.domain.reply.dto.response.ReadMyReplyResponseDto;
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

	/**
	 * 댓글 생성
	 */
	@PostMapping
	public ResponseEntity<BaseResponse<Long>> createReply(@Auth AuthUser user, @PathVariable long boardId,
		@Valid @RequestBody CreateReplyRequestDto dto) {
		long id = 3;
		return ResponseEntity.ok(
			BaseResponse.success(replyService.createReply(id, boardId, dto), ResponseCode.CREATED));
	}

	/**
	 * 댓글 조회
	 * parent_id는 상위 댓글 / 최상단 댓글일 시 null
	 */
	@GetMapping
	public ResponseEntity<BaseResponse<PageResponseDto<ReadReplyResponseDto>>> readRepliesWithParent(
		@PathVariable long boardId,
		@RequestParam(required = false) Long parentId,
		@PageableDefault(size = 5, sort = "createdAt") Pageable pageable) {
		return ResponseEntity.ok(
			BaseResponse.success(replyService.readReplies(boardId, parentId, pageable), ResponseCode.OK));
	}

	/**
	 * 댓글 내용 수정
	 */
	@PatchMapping("/{replyId}")
	public ResponseEntity<BaseResponse<List<Void>>> updateReply(@Auth AuthUser user, @PathVariable long boardId,
		@PathVariable long replyId,
		@RequestBody UpdateReplyRequestDto dto) {
		long id = 3;
		replyService.updateReply(id, boardId, replyId, dto);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.NO_CONTENT));
	}

	/**
	 * 댓글 내용 삭제 상태로 변경
	 */
	@DeleteMapping("/{replyId}")
	public ResponseEntity<BaseResponse<List<Void>>> deleteReply(@Auth AuthUser user, @PathVariable long boardId,
		@PathVariable long replyId) {
		long id = 3;
		replyService.deleteReply(id, boardId, replyId);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.NO_CONTENT));
	}

	/**
	 * 유저 자신이 작성했던 댓글 조회
	 * 추후 url을 고칠 필요가 있음
	 */
	@GetMapping("/me")
	public ResponseEntity<BaseResponse<PageResponseDto<ReadMyReplyResponseDto>>> readRepliesWithUser(
		@Auth AuthUser user, @PageableDefault(size = 5, sort = "createdAt") Pageable pageable) {

		long id = 1;
		return ResponseEntity.ok(
			BaseResponse.success(replyService.readRepliesWithUser(id, pageable), ResponseCode.OK));
	}

	@GetMapping("/me/count-by-category")
	public ResponseEntity<BaseResponse<Map<CategoryType, Long>>> readReplyCountPerCategoryByUser(
		@Auth AuthUser user) {

		long id = 3;
		return ResponseEntity.ok(
			BaseResponse.success(replyService.readReplyCountPerCategoryByUser(id), ResponseCode.OK));
	}
}
