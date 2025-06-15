package com.example.cluvrapi.domain.applicationForm.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.applicationForm.dto.response.InfoSubmissionFormResponseDto;
import com.example.cluvrapi.domain.applicationForm.entity.SubmissionForm;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;

public interface SubmissionFormRepositoryCustom {

	/**
	 * 설명: 클럽 정보와 제출 양식 정보가 일치하는 SubmissionForm 객체를 반환하는 쿼리문
	 *
	 * @param clubId           {설명: 클럽 고유 식별자}
	 * @param submissionFormId {설명: 제출 양식 고유 식별자}
	 * @return Optional<SubmissionForm> {SubmissionForm Optional 객체}
	 * @author {sinyoung0403}
	 */

	Optional<SubmissionForm> findByClubIdAndSubmissionId(Long clubId, Long submissionFormId);

	/**
	 * 설명: 제출 양식을 단건 조회하는 쿼리문
	 *
	 * <p>{Dto Projection 적용}
	 *
	 * @param clubId           {설명: 클럽 고유 식별자}
	 * @param submissionFormId {설명: 제출 양식 고유 식별자}
	 * @return InfoSubmissionFormResponseDto {제출 양식 정보}
	 * @author {sinyoung0403}
	 */

	InfoSubmissionFormResponseDto findSubmissionFormById(Long clubId, Long submissionFormId);

	/**
 * 특정 클럽의 제출 양식 목록을 페이징하여 조회합니다.
 *
 * DTO 프로젝션을 사용하여 제출 양식 정보를 반환하며, 페이징 처리가 적용됩니다.
 *
 * @param clubId 클럽의 고유 식별자
 * @param pageable 페이징 정보를 담은 객체
 * @return 제출 양식 정보의 페이징 결과
 */

	PageResponseDto<InfoSubmissionFormResponseDto> findAllSubmissionFormById(Long clubId, Pageable pageable);

	/****
 * 지정된 클럽 ID에 해당하는 제출 양식 엔티티를 조회합니다.
 *
 * @param clubId 제출 양식을 조회할 클럽의 ID
 * @return 해당 클럽에 연결된 SubmissionForm 엔티티가 존재하면 Optional로 반환하며, 없으면 빈 Optional을 반환합니다.
 */
Optional<SubmissionForm> findSubmissionFormByClubId(Long clubId);
}
