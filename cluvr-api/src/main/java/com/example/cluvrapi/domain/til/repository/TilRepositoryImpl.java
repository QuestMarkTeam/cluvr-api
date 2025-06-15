package com.example.cluvrapi.domain.til.repository;

import static com.example.cluvrapi.domain.til.entity.QTil.til;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.til.dto.response.InfoTilResponseDto;
import com.example.cluvrapi.domain.til.dto.response.QInfoTilResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
public class TilRepositoryImpl implements TilRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public InfoTilResponseDto findTilById(Long tilId) {
		InfoTilResponseDto content = jpaQueryFactory
			.select(new QInfoTilResponseDto(
				til.user.id,    // User 고유 식별자
				til.club.id,    // Club 고유 식별자
				til.title,        // title 제목
				til.content))    // content 내용
			.from(til)
			.where(til.id.eq(tilId).and(til.isDeleted.isFalse()))
			.fetchOne();
		return content;
	}

	@Override
	public PageResponseDto<InfoTilResponseDto> findAllTilById(Long clubId, Pageable pageable) {
		List<InfoTilResponseDto> content = jpaQueryFactory
			.select(new QInfoTilResponseDto(
				til.user.id,    // User 고유 식별자
				til.club.id,    // Club 고유 식별자
				til.title,        // title 제목
				til.content))    // content 내용
			.from(til)
			.where(til.club.id.eq(clubId).and(til.isDeleted.isFalse()))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(til.count())
			.from(til)
			.where(til.club.id.eq(clubId).and(til.isDeleted.isFalse()))
			.fetchOne();

		return PageResponseDto.toDto(new PageImpl<>(content, pageable, total));
	}
}
