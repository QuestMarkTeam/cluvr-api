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
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.CREATED));
	}

	/**
	 * 특정 리뷰 ID에 해당하는 리뷰 정보를 조회하는 API
	 *
	 * @param clubId   클럽 고유 식별자
	 * @param tilId    TIL 고유 식별자
	 * @param reviewId 리뷰 고유 식별자
	 * @return 해당 리뷰 정보 응답
	 * @author sinyoung0403
	 */

	@GetMapping("/{clubId}/tils/{tilId}/reviews/{reviewId}")
	public ResponseEntity<BaseResponse<InfoReviewResponseDto>> findReviewById(
		@PathVariable Long clubId,
		@PathVariable Long tilId,
		@PathVariable Long reviewId
	) {
		InfoReviewResponseDto infoReviewResponseDto = tilReviewService.findReviewById(clubId, tilId, reviewId);
		return ResponseEntity.ok(BaseResponse.success(infoReviewResponseDto, ResponseCode.OK));
	}

	/**
	 * 특정 클럽의 TIL에 대해 작성된 전체 리뷰 목록을 조회하는 API
	 *
	 * @param clubId   클럽 고유 식별자
	 * @param tilId    TIL 고유 식별자
	 * @param pageable 페이지네이션 정보 (기본: size=5, createdAt 기준 정렬)
	 * @return 클럽의 TIL에 작성된 리뷰 목록 (페이지네이션 포함)
	 * @author sinyoung0403
	 */

	@GetMapping("/{clubId}/tils/reviews")
	public ResponseEntity<BaseResponse<PageResponseDto<InfoReviewResponseDto>>> findReviewByClub(
		@PathVariable Long clubId,
		@PathVariable Long tilId,
		@PageableDefault(size = 5, sort = "createdAt") Pageable pageable
	) {
		PageResponseDto<InfoReviewResponseDto> pageResponseDto = tilReviewService.findReviewByClub(clubId, tilId,
			pageable);
		return ResponseEntity.ok(BaseResponse.success(pageResponseDto, ResponseCode.OK));
	}

	/**
	 * 최근 일주일 이내 작성된, 완료(reviewed = true)된 리뷰 목록을 조회하는 API
	 *
	 * @param pageable 페이지네이션 정보 (기본: size=5, createdAt 기준 정렬)
	 * @return 최근 일주일간 완료된 리뷰 목록 (페이지네이션 포함)
	 * @author sinyoung0403
	 */

	@GetMapping("/tils/weekly")
	public ResponseEntity<BaseResponse<PageResponseDto<CompletedReviewResponseDto>>> findWeeklyReview(
		@PageableDefault(size = 5, sort = "createdAt") Pageable pageable
	) {
		PageResponseDto<CompletedReviewResponseDto> pageResponseDto = tilReviewService.findWeeklyReview(pageable);
		return ResponseEntity.ok(BaseResponse.success(pageResponseDto, ResponseCode.OK));
	}
}
