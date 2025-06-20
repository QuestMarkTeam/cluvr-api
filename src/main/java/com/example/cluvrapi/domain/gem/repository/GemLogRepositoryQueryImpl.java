package com.example.cluvrapi.domain.gem.repository;

import static com.example.cluvrapi.domain.gem.entity.QGemLog.gemLog;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;

import com.example.cluvrapi.domain.gem.dto.response.FindGemLogResponseDto;
import com.example.cluvrapi.domain.gem.dto.response.QFindGemLogResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
@RequiredArgsConstructor
public class GemLogRepositoryQueryImpl implements GemLogRepositoryQuery {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<FindGemLogResponseDto> findGemLogByUserId(Long userId) {
		return jpaQueryFactory.select(
				new QFindGemLogResponseDto(gemLog.user.id, gemLog.description, gemLog.amount, gemLog.createdAt,
					gemLog.deletedAt))
			.from(gemLog)
			.where(gemLog.user.id.eq(userId))
			.fetch();
	}
}
