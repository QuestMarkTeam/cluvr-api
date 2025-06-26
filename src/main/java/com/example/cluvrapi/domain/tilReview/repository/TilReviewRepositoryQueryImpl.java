package com.example.cluvrapi.domain.tilReview.repository;

import static com.example.cluvrapi.domain.tilReview.entity.QTilReview.tilReview;

import java.time.LocalDateTime;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

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
}
