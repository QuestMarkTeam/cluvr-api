package com.example.cluvrapi.domain.tilReview.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cluvrapi.domain.common.annotation.Auth;
import com.example.cluvrapi.domain.common.dto.AuthUser;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.tilReview.dto.response.CompletedReviewResponseDto;
import com.example.cluvrapi.domain.tilReview.dto.response.InfoReviewResponseDto;
import com.example.cluvrapi.domain.tilReview.service.TilReviewService;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

/**
 * TIL 리뷰 관련 API를 제공하는 컨트롤러입니다.
 *
 * <p> 주요 기능:
 * - 특정 TIL에 대한 리뷰 요청
 * - 특정 리뷰 단건 조회
 * - 클럽 내 TIL 리뷰 목록 조회
 * - 최근 일주일 이내 완료된 리뷰 목록 조회
 * </p>
 *
 * @author sinyoung0403
 */

@RequestMapping("/api/clubs")
@RestController
@RequiredArgsConstructor
public class TilReviewController {

	private final TilReviewService tilReviewService;

	/**
	 * 특정 TIL에 대해 리뷰 요청을 수행하는 API
	 *
	 * @param authUser 인증된 사용자 정보
	 * @param clubId   클럽 고유 식별자
	 * @param tilId    리뷰를 요청할 TIL 고유 식별자
	 * @return 200 OK (요청 성공), 내부 응답 코드로 CREATED 반환
	 * @author sinyoung0403
	 */

	@PostMapping("/{clubId}/tils/{tilId}/reviews/request")
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

	@GetMapping("/{clubId}/tils/{tilId}/reviews")
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
