package com.example.cluvrapi.domain.point.repository;

import static com.example.cluvrapi.domain.point.entity.QPointLog.pointLog;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;

import com.example.cluvrapi.domain.point.dto.response.FindPointLogResponseDto;
import com.example.cluvrapi.domain.point.dto.response.QFindPointLogResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
@RequiredArgsConstructor
public class PointLogRepositoryQueryImpl implements PointLogRepositoryQuery {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<FindPointLogResponseDto> findPointLogByUserId(Long userId) {
		return jpaQueryFactory.select(
				new QFindPointLogResponseDto(pointLog.user.id, pointLog.description, pointLog.amount, pointLog.createdAt,
					pointLog.deletedAt))
			.from(pointLog)
			.where(pointLog.user.id.eq(userId))
			.fetch();
	}
}
