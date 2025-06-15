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

	/**
	 * 지정된 클럽 ID에 해당하는 모든 제출 양식 정보를 페이지 단위로 조회합니다.
	 *
	 * @param clubId 조회할 클럽의 ID
	 * @param pageable 페이지네이션 정보
	 * @return 제출 양식 정보 DTO의 페이지 응답 객체
	 */
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

	/**
	 * 주어진 클럽 ID에 해당하는 첫 번째 SubmissionForm 엔티티를 조회합니다.
	 *
	 * @param clubId 조회할 클럽의 ID
	 * @return SubmissionForm 엔티티가 존재하면 Optional에 담아 반환하며, 없으면 빈 Optional을 반환합니다.
	 */
	@Override
	public Optional<SubmissionForm> findSubmissionFormByClubId(Long clubId) {
		return Optional.ofNullable(
			jpaQueryFactory.select(submissionForm)
				.from(submissionForm)
				.where(submissionForm.club.id.eq(clubId))
				.fetchFirst()
		);
	}
}
