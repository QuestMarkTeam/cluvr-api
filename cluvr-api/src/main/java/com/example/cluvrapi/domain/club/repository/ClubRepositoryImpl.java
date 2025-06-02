package com.example.cluvrapi.domain.club.repository;

import static com.example.cluvrapi.domain.category.entity.QCategory.category;
import static com.example.cluvrapi.domain.club.entity.QClub.club;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.category.enums.CategoryTargetType;
import com.example.cluvrapi.domain.club.dto.response.FindAllClubResponseDto;
import com.example.cluvrapi.domain.club.dto.response.FindClubResponseDto;
import com.example.cluvrapi.domain.club.dto.response.QFindAllClubResponseDto;
import com.example.cluvrapi.domain.club.dto.response.QFindClubResponseDto;
import com.example.cluvrapi.domain.club.enums.ClubType;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
public class ClubRepositoryImpl implements ClubRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public FindClubResponseDto findClubById(Long clubId) {
		FindClubResponseDto content = jpaQueryFactory.select(
				new QFindClubResponseDto(club.clubType, club.name, category.categoryType.stringValue(), club.greeting,
					club.description, club.posterUrl, club.createdAt))
			.from(club)
			.leftJoin(category)
			.on(category.targetType.eq(CategoryTargetType.CLUB).and(category.targetId.eq(clubId)))
			.where(club.id.eq(clubId))
			.fetchOne();
		return content;
	}

	@Override
	public PageResponseDto<FindAllClubResponseDto> findAllClub(ClubType clubType, Pageable pageable) {
		List<FindAllClubResponseDto> content = jpaQueryFactory.select(
				new QFindAllClubResponseDto(club.id, club.user.id, club.name, club.clubType, category.categoryType,
					club.greeting, club.posterUrl, club.maxMemberCount))
			.from(club)
			.leftJoin(category)
			.on(category.targetType.eq(CategoryTargetType.CLUB).and(category.targetId.eq(club.id)))
			.where(club.clubType.eq(clubType))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory.select(club.count())
			.from(club)
			.leftJoin(category)
			.on(category.targetType.eq(CategoryTargetType.CLUB).and(category.targetId.eq(club.id)))
			.where(club.clubType.eq(clubType))
			.fetchOne();

		return PageResponseDto.toDto(new PageImpl<>(content, pageable, total));
	}
}
