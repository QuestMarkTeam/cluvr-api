package com.example.cluvrapi.domain.applicationForm.repository;

import static com.example.cluvrapi.domain.applicationForm.entity.QProblemForm.problemForm;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.applicationForm.dto.response.InfoProblemFormResponseDto;
import com.example.cluvrapi.domain.applicationForm.dto.response.QInfoProblemFormResponseDto;
import com.example.cluvrapi.domain.applicationForm.entity.ProblemForm;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
public class ProblemFormRepositoryImpl implements ProblemFormRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Optional<ProblemForm> findByClubIdAndProblemFormId(Long clubId, Long problemId) {
		return Optional.ofNullable(
			jpaQueryFactory.selectFrom(problemForm)
				.where(problemForm.club.id.eq(clubId).and(problemForm.id.eq(problemId)))
				.fetchOne()
		);
	}

	@Override
	public InfoProblemFormResponseDto findProblemFormById(Long clubId, Long problemId) {
		InfoProblemFormResponseDto content = jpaQueryFactory
			.select(new QInfoProblemFormResponseDto(
				problemForm.id,
				problemForm.problemTemplate,
				problemForm.submissionInstructions,
				problemForm.gradingCriteria,
				problemForm.isActive
			))
			.from(problemForm)
			.where(problemForm.id.eq(problemId).and(problemForm.club.id.eq(clubId)))
			.fetchOne();
		return content;
	}

	@Override
	public PageResponseDto<InfoProblemFormResponseDto> findByProblemFormAllById(Long clubId, Pageable pageable) {
		List<InfoProblemFormResponseDto> content = jpaQueryFactory
			.select(new QInfoProblemFormResponseDto(
				problemForm.id,
				problemForm.problemTemplate,
				problemForm.submissionInstructions,
				problemForm.gradingCriteria,
				problemForm.isActive
			))
			.from(problemForm)
			.where(problemForm.club.id.eq(clubId))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(problemForm.count())
			.from(problemForm)
			.where(problemForm.club.id.eq(clubId))
			.fetchOne();

		return PageResponseDto.toDto(new PageImpl<>(content, pageable, total));
	}

	@Override
	public Optional<ProblemForm> findActiveProblemFormIdByClubId(Long clubId) {
		return Optional.ofNullable(
			jpaQueryFactory.select(problemForm)
				.from(problemForm)
				.where(
					problemForm.club.id.eq(clubId)
						.and(problemForm.isDeleted.eq(false))
						.and(problemForm.isActive.eq(true))
				)
				.fetchOne()
		);
	}
}
