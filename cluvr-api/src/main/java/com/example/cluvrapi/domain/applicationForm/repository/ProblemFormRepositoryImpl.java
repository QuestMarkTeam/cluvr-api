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

	/**
	 * 주어진 동아리 ID와 문제 폼 ID에 해당하는 ProblemForm의 정보를 InfoProblemFormResponseDto로 반환합니다.
	 *
	 * @param clubId 조회할 동아리의 ID
	 * @param problemId 조회할 문제 폼의 ID
	 * @return 해당 ProblemForm의 정보가 담긴 InfoProblemFormResponseDto, 존재하지 않으면 null
	 */
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

	/**
	 * 지정된 클럽 ID에 해당하는 모든 ProblemForm 엔티티를 페이지 단위로 조회하여 반환합니다.
	 *
	 * @param clubId 조회할 클럽의 식별자
	 * @param pageable 페이지네이션 정보
	 * @return InfoProblemFormResponseDto의 페이지 응답 객체
	 */
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

	/**
	 * 주어진 클럽 ID에 해당하는 활성화된(삭제되지 않은) ProblemForm 엔티티를 반환합니다.
	 *
	 * @param clubId 클럽의 식별자
	 * @return 활성화된 ProblemForm이 존재하면 Optional에 담아 반환하며, 없으면 Optional.empty()를 반환합니다.
	 */
	@Override
	public Optional<ProblemForm> findActiveProblemFormByClubId(Long clubId) {
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
