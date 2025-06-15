package com.example.cluvrapi.domain.notice.repository;

import static com.example.cluvrapi.domain.notice.entity.QNotice.notice;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.notice.dto.response.InfoNoticeResponseDto;
import com.example.cluvrapi.domain.notice.dto.response.QInfoNoticeResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Optional<InfoNoticeResponseDto> findNoticeById(Long clubId, Long noticeId) {
		return Optional.ofNullable(
			jpaQueryFactory
				.select(new QInfoNoticeResponseDto(
					notice.id,
					notice.user.id,
					notice.title,
					notice.content
				))
				.from(notice)
				.where(notice.id.eq(noticeId).and(notice.club.id.eq(clubId)))
				.fetchOne()
		);
	}

	@Override
	public PageResponseDto<InfoNoticeResponseDto> findAllNotice(Long clubId, Pageable pageable) {
		List<InfoNoticeResponseDto> content = jpaQueryFactory
			.select(new QInfoNoticeResponseDto(
				notice.id,
				notice.user.id,
				notice.title,
				notice.content
			))
			.from(notice)
			.where(notice.club.id.eq(clubId))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(notice.count())
			.from(notice)
			.where(notice.club.id.eq(clubId))
			.fetchOne();

		return PageResponseDto.toDto(new PageImpl<>(content, pageable, total));
	}
}
