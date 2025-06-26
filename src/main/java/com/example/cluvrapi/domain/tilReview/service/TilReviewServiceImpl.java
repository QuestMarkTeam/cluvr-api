package com.example.cluvrapi.domain.tilReview.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.example.cluvrapi.domain.til.entity.Til;
import com.example.cluvrapi.domain.til.repository.TilRepository;
import com.example.cluvrapi.domain.tilReview.entity.TilReview;
import com.example.cluvrapi.domain.tilReview.repository.TilReviewRepository;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.ResponseCode;

@Service
@RequiredArgsConstructor
public class TilReviewServiceImpl implements TilReviewService {

	private final TilRepository tilRepository;
	private final TilReviewRepository tilReviewRepository;

	@Transactional
	@Override
	public void requestReview(Long userId, Long tilId, Long clubId) {
		// 1) Til 조회, Club 조회
		Til findTil = tilRepository.findTilByIdAndClubId(clubId, tilId).orElseThrow(
			() -> new BusinessException(ResponseCode.NOT_FOUND, "해당 TIL이 존재하지 않습니다.")
		);

		// 2) 리뷰 요청 여부 검증
		validateReview(LocalDate.now(),clubId);

		// 3) Til Review Document 생성
		TilReview tilReview = new TilReview(
			userId,
			clubId,
			tilId,
			findTil.getContent()
		);

		// 4) DB 저장
		tilReviewRepository.save(tilReview);
	}

	public void validateReview(LocalDate today, Long clubId) {
		LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
		LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

		LocalDateTime startDateTime = startOfWeek.atStartOfDay();
		LocalDateTime endDateTime = endOfWeek.atTime(23, 59, 59);

		boolean alreadyRequested = tilReviewRepository
			.findLatestReviewInPeriod(clubId, startDateTime, endDateTime)
			.isPresent();

		if (alreadyRequested) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "이번 주에 이미 리뷰 요청을 했습니다.");
		}
	}
}
