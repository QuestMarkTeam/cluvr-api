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
	 * 설명: 제출 양식을 다건 조회하는 쿼리문
	 *
	 * <p>{Dto Projection 적용, 페이징 처리}
	 *
	 * @param clubId   {설명: 클럽 고유 식별자}
	 * @param pageable {설명: 페이징 객체}
	 * @return PageResponseDto<InfoSubmissionFormResponseDto> {제출 양식 정보 페이징 객체}
	 * @author {sinyoung0403}
	 */

	PageResponseDto<InfoSubmissionFormResponseDto> findAllSubmissionFormById(Long clubId, Pageable pageable);

	Optional<SubmissionForm> findSubmissionFormByClubId(Long clubId);
}
