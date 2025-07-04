package com.example.cluvrapi.domain.tilReview.service;

import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.tilReview.dto.response.CompletedReviewResponseDto;
import com.example.cluvrapi.domain.tilReview.dto.response.InfoReviewResponseDto;
import com.example.cluvrapi.global.annotation.IsClubAdmin;
import com.example.cluvrapi.global.annotation.IsClubMember;
import com.example.cluvrapi.global.exception.BusinessException;

public interface TilReviewService {

	/**
	 * 특정 TIL에 대해 리뷰 요청을 수행하는 메서드
	 *
	 * @param userId 유저 고유 식별자
	 * @param clubId 클럽 고유 식별자
	 * @param tilId  리뷰를 요청할 TIL의 고유 식별자
	 * @throws BusinessException 존재하지 않는 클럽, TIL 또는 권한 없음 등의 경우 404 NotFound 또는 403 Forbidden 예외 발생
	 * @author sinyoung0403
	 */

	@IsClubAdmin
	void requestReview(Long userId, Long clubId, Long tilId);

	/**
	 * 특정 리뷰 ID에 해당하는 리뷰 정보를 조회하는 메서드
	 *
	 * @param clubId   클럽 고유 식별자
	 * @param tilId    TIL 고유 식별자
	 * @param reviewId 조회할 리뷰 고유 식별자
	 * @return 조회된 리뷰 정보 (reviewed 여부와 관계 없음)
	 * @throws BusinessException 존재하지 않는 클럽, TIL, 리뷰 등의 경우 404 NotFound 예외 발생
	 * @author sinyoung0403
	 */

	InfoReviewResponseDto findReviewById(Long clubId, Long tilId, Long reviewId);

	/**
	 * 특정 클럽의 TIL에 대해 작성된 리뷰 목록을 조회하는 메서드
	 *
	 * @param clubId 클럽 고유 식별자
	 * @param tilId  TIL 고유 식별자
	 * @return 해당 클럽 TIL에 작성된 전체 리뷰 목록 (페이지네이션 적용)
	 * @author sinyoung0403
	 */

	@IsClubMember
	PageResponseDto<InfoReviewResponseDto> findReviewByClub(Long clubId, Long tilId, Pageable pageable);

	/**
	 * 일주일 이내 작성된 '리뷰 완료된' 리뷰 목록을 조회하는 메서드
	 *
	 * <p>조회 대상은 전체 클럽 중 reviewed 상태가 true이며, 작성일이 최근 7일 이내인 리뷰입니다.
	 * 클럽 관리자나 리뷰 관리 권한이 있는 사용자만 호출할 수 있습니다.
	 *
	 * @return 최근 일주일간 reviewed 상태가 true인 리뷰 목록 (페이지네이션 포함)
	 * @author sinyoung0403
	 */

	PageResponseDto<CompletedReviewResponseDto> findWeeklyReview(Pageable pageable);
}
