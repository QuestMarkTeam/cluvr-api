package com.example.cluvrapi.domain.tilReview.repository;

import java.time.LocalDateTime;
import java.util.Optional;

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
}
