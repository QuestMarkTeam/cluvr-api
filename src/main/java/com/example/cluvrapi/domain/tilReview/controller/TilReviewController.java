package com.example.cluvrapi.domain.tilReview.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cluvrapi.domain.common.annotation.Auth;
import com.example.cluvrapi.domain.common.dto.AuthUser;
import com.example.cluvrapi.domain.tilReview.service.TilReviewService;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

@RequestMapping("/api/clubs/{clubId}/til")
@RestController
@RequiredArgsConstructor
public class TilReviewController {

	private final TilReviewService tilReviewService;

	@PostMapping("/{tilId}/request-review")
	public ResponseEntity<BaseResponse<Void>> requestReview(
		@Auth AuthUser authUser,
		@PathVariable Long clubId,
		@PathVariable Long tilId
	) {
		tilReviewService.requestReview(authUser.id(), clubId, tilId);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.NO_CONTENT));
	}
}
