package com.example.cluvrapi.domain.club.repository;

import static com.example.cluvrapi.domain.category.entity.QCategory.category;
import static com.example.cluvrapi.domain.club.entity.QClub.club;
import static com.example.cluvrapi.domain.clubMember.entity.QClubMember.clubMember;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.category.enums.CategoryTargetType;
import com.example.cluvrapi.domain.club.dto.response.FindAllClubResponseDto;
import com.example.cluvrapi.domain.club.dto.response.FindClubResponseDto;
import com.example.cluvrapi.domain.club.dto.response.QFindAllClubResponseDto;
import com.example.cluvrapi.domain.club.dto.response.QFindClubResponseDto;
import com.example.cluvrapi.domain.club.enums.ClubType;
import com.example.cluvrapi.domain.clubMember.entity.enums.ClubMemberRole;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;

/**
 * ClubRepositoryQuery 인터페이스를 구현한 클래스입니다.
 *
 * <p> QueryDSL 을 활용하여 클럽 단건 조회 및 클럽 리스트 조회 (페이징 포함) 기능을 제공합니다.
 *
 * @author sinyoung0403
 */

@RequiredArgsConstructor
public class ClubRepositoryQueryImpl implements ClubRepositoryQuery {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public boolean existsByClubName(String name) {
		return jpaQueryFactory
			.selectOne()
			.from(club)
			.where(club.name.eq(name))
			.fetchFirst() != null;
	}

	@Override
	public Optional<FindClubResponseDto> findClubById(Long clubId) {
		return Optional.ofNullable(
			jpaQueryFactory
				.select(
					new QFindClubResponseDto(
						club.clubType,
						club.name,
						category.categoryType.stringValue(),
						club.greeting,
						club.description,
						club.joinType,
						club.posterUrl,
						club.createdAt)
				)
				.from(club)
				.leftJoin(category)
				.on(category.targetType.eq(CategoryTargetType.CLUB).and(category.targetId.eq(clubId)))
				.where(club.id.eq(clubId))
				.fetchOne()
		);
	}

	@Override
	public PageResponseDto<FindAllClubResponseDto> findAllClub(ClubType clubType, Pageable pageable) {
		List<FindAllClubResponseDto> content = jpaQueryFactory.select(
				new QFindAllClubResponseDto(club.id, clubMember.user.id, club.name, club.clubType, category.categoryType,
					club.greeting, club.posterUrl, club.maxMemberCount, club.minCloverRequirement))
			.from(club)
			.leftJoin(clubMember)
			.on(clubMember.club.eq(club).and(clubMember.clubMemberRole.eq(ClubMemberRole.OWNER)))
			.leftJoin(category)
			.on(category.targetType.eq(CategoryTargetType.CLUB).and(category.targetId.eq(club.id)))
			.where(club.clubType.eq(clubType), ClubQueryFilter.notDeleted(), ClubQueryFilter.publicOnly())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory.select(club.count())
			.from(club)
			.leftJoin(clubMember)
			.on(clubMember.club.eq(club).and(clubMember.clubMemberRole.eq(ClubMemberRole.OWNER)))
			.leftJoin(category)
			.on(category.targetType.eq(CategoryTargetType.CLUB).and(category.targetId.eq(club.id)))
			.where(club.clubType.eq(clubType), ClubQueryFilter.notDeleted(), ClubQueryFilter.publicOnly())
			.fetchOne();

		return PageResponseDto.toDto(new PageImpl<>(content, pageable, total));
	}
}
