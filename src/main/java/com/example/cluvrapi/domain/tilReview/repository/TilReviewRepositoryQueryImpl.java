package com.example.cluvrapi.domain.tilReview.repository;

import static com.example.cluvrapi.domain.tilReview.entity.QTilReview.tilReview;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.tilReview.dto.response.CompletedReviewResponseDto;
import com.example.cluvrapi.domain.tilReview.dto.response.InfoReviewResponseDto;
import com.example.cluvrapi.domain.tilReview.dto.response.QCompletedReviewResponseDto;
import com.example.cluvrapi.domain.tilReview.dto.response.QInfoReviewResponseDto;
import com.example.cluvrapi.domain.tilReview.entity.TilReview;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
public class TilReviewRepositoryQueryImpl implements TilReviewRepositoryQuery {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Optional<TilReview> findLatestReviewInPeriod(Long clubId, LocalDateTime start, LocalDateTime end) {
		return Optional.ofNullable(
			jpaQueryFactory.selectFrom(tilReview)
				.where(
					tilReview.clubId.eq(clubId),
					tilReview.createdAt.between(start, end)
				)
				.orderBy(tilReview.createdAt.desc())
				.limit(1)
				.fetchOne()
		);
	}

	@Override
	public Optional<InfoReviewResponseDto> findReviewById(Long clubId, Long tilId, Long reviewId) {
		return Optional.ofNullable(
			jpaQueryFactory.select(
					new QInfoReviewResponseDto(
						tilReview.id,
						tilReview.clubId,
						tilReview.tilId,
						tilReview.tilContent,
						tilReview.reviewed,
						tilReview.score,
						tilReview.summary,
						tilReview.feedback
					))
				.from(tilReview)
				.where(tilReview.clubId.eq(clubId)
					.and(tilReview.tilId.eq(tilId))
					.and(tilReview.id.eq(reviewId)))
				.fetchOne()
		);
	}

	@Override
	public PageResponseDto<InfoReviewResponseDto> findReviewByClub(Long clubId, Pageable pageable) {
		List<InfoReviewResponseDto> content = jpaQueryFactory
			.select(
				new QInfoReviewResponseDto(
					tilReview.id,
					tilReview.clubId,
					tilReview.tilId,
					tilReview.tilContent,
					tilReview.reviewed,
					tilReview.score,
					tilReview.summary,
					tilReview.feedback
				))
			.from(tilReview)
			.where(tilReview.clubId.eq(clubId))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(tilReview.count())
			.from(tilReview)
			.where(tilReview.clubId.eq(clubId))
			.fetchOne();

		return PageResponseDto.toDto(new PageImpl<>(content, pageable, total));
	}

	@Override
	public PageResponseDto<CompletedReviewResponseDto> findWeeklyReview(LocalDateTime startDateTime,
		LocalDateTime endDateTime, Pageable pageable) {

		List<CompletedReviewResponseDto> content = jpaQueryFactory
			.select(
				new QCompletedReviewResponseDto(
					tilReview.id,
					tilReview.clubId,
					tilReview.tilId,
					tilReview.tilContent,
					tilReview.score,
					tilReview.summary,
					tilReview.feedback
				)
			)
			.from(tilReview)
			.where(
				tilReview.createdAt.between(startDateTime, endDateTime)
					.and(tilReview.reviewed.isTrue())
			)
			.orderBy(tilReview.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(tilReview.count())
			.from(tilReview)
			.where(
				tilReview.createdAt.between(startDateTime, endDateTime)
					.and(tilReview.reviewed.isTrue())
			)
			.fetchOne();

		return PageResponseDto.toDto(new PageImpl<>(content, pageable, total));
	}
}
