package com.example.cluvrapi.domain.join.repository;

import static com.example.cluvrapi.domain.join.entity.QJoinRequest.joinRequest;
import static com.example.cluvrapi.domain.join.entity.QJoinRequestAnswer.joinRequestAnswer;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.join.dto.response.InfoJoinRequestResponseDto;
import com.example.cluvrapi.domain.join.dto.response.MyClubJoinResponseDto;
import com.example.cluvrapi.domain.join.dto.response.MyJoinRequestResponseDto;
import com.example.cluvrapi.domain.join.dto.response.QInfoJoinRequestResponseDto;
import com.example.cluvrapi.domain.join.dto.response.QMyClubJoinResponseDto;
import com.example.cluvrapi.domain.join.dto.response.QMyJoinRequestResponseDto;
import com.example.cluvrapi.domain.join.entity.JoinRequest;
import com.example.cluvrapi.domain.join.entity.JoinRequestAnswer;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

/**
 * JoinRequestRepositoryQuery 인터페이스의 구현체 클래스 입니다.
 */

@RequiredArgsConstructor
public class JoinRequestRepositoryQueryImpl implements JoinRequestRepositoryQuery {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public boolean existsJoinByClubIdAndUserId(Long clubId, Long userId) {
		return jpaQueryFactory
			.selectOne()
			.from(joinRequest)
			.where(joinRequest.club.id.eq(clubId).and(joinRequest.user.id.eq(userId)))
			.fetchFirst() != null;
	}

	@Override
	public PageResponseDto<MyClubJoinResponseDto> findJoinRequestByClubId(Long clubId, Pageable pageable) {
		List<MyClubJoinResponseDto> content = jpaQueryFactory
			.select(new QMyClubJoinResponseDto(
				joinRequest.id,
				joinRequest.user.id,
				joinRequest.joinStatus,
				joinRequest.joinType))
			.from(joinRequest)
			.where(joinRequest.club.id.eq(clubId))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(joinRequest.count())
			.from(joinRequest)
			.where(joinRequest.club.id.eq(clubId))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetchOne();

		return PageResponseDto.toDto(new PageImpl<>(content, pageable, total));
	}

	@Override
	public PageResponseDto<MyJoinRequestResponseDto> findMyJoinRequests(Long userId, Pageable pageable) {
		List<MyJoinRequestResponseDto> content = jpaQueryFactory
			.select(new QMyJoinRequestResponseDto(
				joinRequest.id,
				joinRequest.club.id,
				joinRequest.joinStatus,
				joinRequest.joinType))
			.from(joinRequest)
			.where(joinRequest.club.id.eq(userId))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(joinRequest.count())
			.from(joinRequest)
			.where(joinRequest.user.id.eq(userId))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetchOne();

		return PageResponseDto.toDto(new PageImpl<>(content, pageable, total));
	}

	@Override
	public InfoJoinRequestResponseDto findJoinRequestById(Long clubId, Long joinRequestId) {
		InfoJoinRequestResponseDto content = jpaQueryFactory
			.select(
				new QInfoJoinRequestResponseDto(
					joinRequest.id,
					joinRequest.club.id,
					joinRequest.user.id,
					joinRequest.joinType,
					new CaseBuilder()
						.when(joinRequestAnswer.answer.isNotNull())
						.then(joinRequestAnswer.answer)
						.otherwise("null")
				)
			)
			.from(joinRequest)
			.leftJoin(joinRequestAnswer)
			.on(joinRequest.id.eq(joinRequestAnswer.joinRequest.id))
			.where(joinRequest.club.id.eq(clubId).and(joinRequest.id.eq(joinRequestId)))
			.fetchOne();

		return content;
	}

	@Override
	public Optional<JoinRequestAnswer> findJoinRequestAnswerByIdAndClubId(Long clubId, Long joinRequestId) {
		return Optional.ofNullable(jpaQueryFactory.selectFrom(joinRequestAnswer)
			.where(joinRequestAnswer.joinRequest.club.id.eq(clubId)
				.and(joinRequestAnswer.joinRequest.id.eq(joinRequestId)))
			.fetchOne());
	}

	@Override
	public Optional<JoinRequest> joinRequestByIdAndClubId(Long clubId, Long joinRequestId) {
		return Optional.ofNullable(jpaQueryFactory.selectFrom(joinRequest)
			.where(joinRequest.club.id.eq(clubId)
				.and(joinRequest.id.eq(joinRequestId)))
			.fetchOne());
	}
}
