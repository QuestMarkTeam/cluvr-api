package com.example.cluvrapi.domain.applicationForm.repository;

import static com.example.cluvrapi.domain.applicationForm.entity.QSubmissionForm.submissionForm;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.applicationForm.dto.response.InfoSubmissionFormResponseDto;
import com.example.cluvrapi.domain.applicationForm.dto.response.QInfoSubmissionFormResponseDto;
import com.example.cluvrapi.domain.applicationForm.entity.SubmissionForm;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
public class SubmissionFormRepositoryImpl implements SubmissionFormRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Optional<SubmissionForm> findByClubIdAndSubmissionId(Long clubId, Long submissionId) {
		return Optional.ofNullable(
			jpaQueryFactory.selectFrom(submissionForm)
				.where(submissionForm.club.id.eq(clubId).and(submissionForm.id.eq(submissionId)))
				.fetchOne()
		);
	}

	@Override
	public InfoSubmissionFormResponseDto findSubmissionFormById(Long clubId, Long submissionFormId) {
		InfoSubmissionFormResponseDto content = jpaQueryFactory
			.select(new QInfoSubmissionFormResponseDto(submissionForm.submissionTemplate))
			.from(submissionForm)
			.where(submissionForm.id.eq(submissionFormId).and(submissionForm.club.id.eq(clubId)))
			.fetchOne();
		return content;
	}

	@Override
	public PageResponseDto<InfoSubmissionFormResponseDto> findAllSubmissionFormById(Long clubId, Pageable pageable) {
		List<InfoSubmissionFormResponseDto> content = jpaQueryFactory
			.select(new QInfoSubmissionFormResponseDto(submissionForm.submissionTemplate))
			.from(submissionForm)
			.where(submissionForm.club.id.eq(clubId))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(submissionForm.count())
			.from(submissionForm)
			.where(submissionForm.club.id.eq(clubId))
			.fetchOne();

		return PageResponseDto.toDto(new PageImpl<>(content, pageable, total));
	}
}
