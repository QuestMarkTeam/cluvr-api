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
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

/**
 * JoinRequestRepositoryQuery 인터페이스의 구현체 클래스 입니다.
 */

@RequiredArgsConstructor
public class JoinRequestRepositoryQueryImpl implements JoinRequestRepositoryQuery {

	private final JPAQueryFactory jpaQueryFactory;

	/**
	 * 주어진 클럽 ID와 사용자 ID에 해당하며 삭제되지 않은 가입 요청을 조회합니다.
	 *
	 * @param clubId 조회할 클럽의 ID
	 * @param userId 조회할 사용자의 ID
	 * @return 조건에 맞는 가입 요청이 존재하면 Optional에 담아 반환하며, 없으면 빈 Optional을 반환합니다.
	 */
	@Override
	public Optional<JoinRequest> findJoinByClubIdAndUserId(Long clubId, Long userId) {
		return Optional.ofNullable(
			jpaQueryFactory
				.selectFrom(joinRequest)
				.where(joinRequest.club.id.eq(clubId)
					.and(joinRequest.user.id.eq(userId))
					.and(joinRequest.isDeleted.isFalse()))
				.fetchOne()
		);
	}

	/**
	 * 지정된 클럽의 모든 가입 요청 목록을 페이지네이션하여 반환합니다.
	 *
	 * @param clubId 조회할 클럽의 ID
	 * @param pageable 페이지네이션 정보
	 * @return 클럽의 가입 요청 목록과 페이지네이션 정보를 담은 PageResponseDto
	 */
	@Override
	public PageResponseDto<MyClubJoinResponseDto> findJoinRequestByClubId(Long clubId, Pageable pageable) {
		List<MyClubJoinResponseDto> content = jpaQueryFactory
			.select(new QMyClubJoinResponseDto(
				joinRequest.id,
				joinRequest.user.id,
				joinRequest.joinStatus,
				joinRequest.joinType))
			.from(joinRequest)
			.where(joinRequest.club.id.eq(clubId).and(joinRequest.isDeleted.isFalse()))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(joinRequest.count())
			.from(joinRequest)
			.where(joinRequest.club.id.eq(clubId).and(joinRequest.isDeleted.isFalse()))
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
			.where(joinRequest.user.id.eq(userId).and(joinRequest.isDeleted.isFalse()))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(joinRequest.count())
			.from(joinRequest)
			.where(joinRequest.user.id.eq(userId).and(joinRequest.isDeleted.isFalse()))
			.fetchOne();

		return PageResponseDto.toDto(new PageImpl<>(content, pageable, total));
	}

	@Override
	public Optional<InfoJoinRequestResponseDto> findJoinRequestById(Long clubId, Long joinRequestId) {
		return Optional.ofNullable(
			jpaQueryFactory
				.select(
					new QInfoJoinRequestResponseDto(
						joinRequest.id,
						joinRequest.club.id,
						joinRequest.user.id,
						joinRequest.joinType,
						new CaseBuilder()
							.when(joinRequestAnswer.answer.isNotNull())
							.then(joinRequestAnswer.answer)
							.otherwise(Expressions.nullExpression(String.class))
					)
				)
				.from(joinRequest)
				.leftJoin(joinRequestAnswer)
				.on(joinRequest.id.eq(joinRequestAnswer.joinRequest.id))
				.where(joinRequest.club.id.eq(clubId)
					.and(joinRequest.id.eq(joinRequestId))
					.and(joinRequest.isDeleted.isFalse()))
				.fetchOne()
		);
	}

	@Override
	public Optional<JoinRequestAnswer> findJoinRequestAnswerByIdAndClubId(Long clubId, Long joinRequestId) {
		return Optional.ofNullable(jpaQueryFactory.selectFrom(joinRequestAnswer)
			.where(joinRequestAnswer.joinRequest.club.id.eq(clubId)
				.and(joinRequestAnswer.joinRequest.id.eq(joinRequestId))
				.and(joinRequestAnswer.joinRequest.isDeleted.isFalse()))
			.fetchOne());
	}

	@Override
	public Optional<JoinRequest> joinRequestByIdAndClubId(Long clubId, Long joinRequestId) {
		return Optional.ofNullable(
			jpaQueryFactory.selectFrom(joinRequest)
				.where(joinRequest.club.id.eq(clubId)
					.and(joinRequest.id.eq(joinRequestId))
					.and(joinRequest.isDeleted.isFalse()))
				.fetchOne());
	}
}
