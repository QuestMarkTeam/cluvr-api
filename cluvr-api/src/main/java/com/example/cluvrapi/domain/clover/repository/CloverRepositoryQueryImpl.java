package com.example.cluvrapi.domain.clover.repository;

import static com.example.cluvrapi.domain.clover.entity.QClover.clover;
import static com.example.cluvrapi.domain.clover.entity.QCloverLog.cloverLog;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.example.cluvrapi.domain.clover.dto.FindCloverLogResponseDto;
import com.example.cluvrapi.domain.clover.dto.FindCloverResponseDto;
import com.example.cluvrapi.domain.clover.dto.QFindCloverLogResponseDto;
import com.example.cluvrapi.domain.clover.dto.QFindCloverResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Service
@RequiredArgsConstructor
public class CloverRepositoryQueryImpl implements CloverRepositoryQuery {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public FindCloverResponseDto findScoreByUserId(Long userId) {
		return jpaQueryFactory.select(new QFindCloverResponseDto(clover.score, clover.tier, clover.user.id))
			.from(clover)
			.where(clover.user.id.eq(userId)).fetchOne();
	}

	@Override
	public List<FindCloverLogResponseDto> findCloverLogByUserId(Long userId) {
		return jpaQueryFactory.select(
				new QFindCloverLogResponseDto(cloverLog.user.id, cloverLog.description, cloverLog.amount,
					cloverLog.createdAt,
					cloverLog.deletedAt))
			.from(cloverLog)
			.where(cloverLog.user.id.eq(userId)).fetch();
	}
}
