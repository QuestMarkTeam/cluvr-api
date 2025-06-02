package com.example.cluvrapi.domain.rank.repository;

import static com.example.cluvrapi.domain.rank.entity.QRank.rank;
import static com.example.cluvrapi.domain.rank.entity.QRankLog.rankLog;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.example.cluvrapi.domain.rank.dto.FindRankLogResponseDto;
import com.example.cluvrapi.domain.rank.dto.FindRankResponseDto;
import com.example.cluvrapi.domain.rank.dto.QFindRankLogResponseDto;
import com.example.cluvrapi.domain.rank.dto.QFindRankResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Service
@RequiredArgsConstructor
public class RankRepositoryQueryImpl implements RankRepositoryQuery {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public FindRankResponseDto findScoreByUserId(Long userId) {
		return jpaQueryFactory.select(new QFindRankResponseDto(rank.score, rank.tier, rank.user.id))
			.from(rank)
			.where(rank.user.id.eq(userId)).fetchOne();
	}

	@Override
	public FindRankLogResponseDto findRankLogByUserId(Long userId) {
		return jpaQueryFactory.select(
				new QFindRankLogResponseDto(rankLog.user.id, rankLog.description, rankLog.amount, rankLog.createdAt,
					rankLog.deletedAt))
			.from(rankLog)
			.where(rankLog.user.id.eq(userId)).fetchOne();
	}
}
