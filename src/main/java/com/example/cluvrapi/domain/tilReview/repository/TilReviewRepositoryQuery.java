package com.example.cluvrapi.domain.tilReview.repository;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.Optional;

import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.tilReview.dto.response.CompletedReviewResponseDto;
import com.example.cluvrapi.domain.tilReview.dto.response.InfoReviewResponseDto;
import com.example.cluvrapi.domain.tilReview.entity.TilReview;

public interface TilReviewRepositoryQuery {

	/**
	 * 특정 기간 내에서 해당 클럽의 가장 최근 TilReview 를 조회하는 메서드
	 *
	 * <p> 기간 조건에 해당하는 TilReview 중 가장 최근(createdAt 기준) 데이터 반환
	 *
	 * @param clubId 클럽 고유 식별
	 * @param start  조회 시작 일시
	 * @param end    조회 종료 일시
	 * @return Optional<TilReview> 가장 최근 TilReview 정보 (없을 수 있음)
	 * @author sinyoung0403
	 */

	Optional<TilReview> findLatestReviewInPeriod(Long clubId, LocalDateTime start, LocalDateTime end);

	/**
	 * 특정 클럽의 특정 TIL에 대한 특정 리뷰를 조회하는 메서드
	 *
	 * @param clubId   클럽 고유 식별자
	 * @param tilId    TIL 고유 식별자
	 * @param reviewId 리뷰 고유 식별자
	 * @return Optional<InfoReviewResponseDto> 해당 리뷰의 상세 정보 (존재하지 않을 수 있음)
	 * @author sinyoung0403
	 */

	Optional<InfoReviewResponseDto> findReviewById(Long clubId, Long tilId, Long reviewId);

	/**
	 * 특정 클럽의 TIL에 대한 리뷰 목록을 페이지 단위로 조회하는 메서드
	 *
	 * @param clubId   클럽 고유 식별자
	 * @param pageable 페이지 정보 (page, size 등)
	 * @return PageResponseDto<InfoReviewResponseDto> 리뷰 목록 페이징 응답 DTO
	 * @author sinyoung0403
	 */

	PageResponseDto<InfoReviewResponseDto> findReviewByClub(Long clubId, Pageable pageable);

	/**
	 * 지정된 주간 기간 내 완료된 리뷰 목록을 페이징 조회하는 메서드
	 *
	 * @param startDateTime 주간 시작 시각
	 * @param endDateTime   주간 종료 시각
	 * @param pageable      페이지 정보
	 * @return PageResponseDto<CompletedReviewResponseDto> 완료된 리뷰 페이징 목록 응답 DTO
	 * @author sinyoung0403
	 */

	PageResponseDto<CompletedReviewResponseDto> findWeeklyReview(LocalDateTime startDateTime,
		LocalDateTime endDateTime,
		Pageable pageable);
}
