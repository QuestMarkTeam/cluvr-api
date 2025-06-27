package com.example.cluvrapi.domain.applicationForm.service;

import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.applicationForm.dto.request.CreateSubmissionFormRequestDto;
import com.example.cluvrapi.domain.applicationForm.dto.request.UpdateSubmissionTemplateRequestDto;
import com.example.cluvrapi.domain.applicationForm.dto.response.CreateSubmissionFormResponseDto;
import com.example.cluvrapi.domain.applicationForm.dto.response.InfoSubmissionFormResponseDto;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.global.annotation.IsClubOwner;
import com.example.cluvrapi.global.exception.BusinessException;

public interface SubmissionFormService {

	/**
	 * 설명: 클럽의 제출 양식을 생성하는 메서드
	 *
	 * @param userId                   {설명: 클럽의 고유 식별자}
	 * @param clubId                   {설명: 클럽의 고유 식별자}
	 * @param submissionFormRequestDto {설명: 가입 제출 양식에 필요한 정보}
	 * @return CreateSubmissionFormResponseDto {설명: 클럽의 id 값 반환}
	 * @throws BusinessException {404 NotFound}
	 * @author sinyoung0403
	 */

	@IsClubOwner
	CreateSubmissionFormResponseDto createSubmissionForm(Long userId,
		Long clubId, CreateSubmissionFormRequestDto submissionFormRequestDto);

	/**
	 * 설명: 클럽의 제출양식을 단건조회하는 메서드
	 *
	 * @param clubId           {설명: 클럽 고유 식별자}
	 * @param submissionFormId {설명: 제출 양식 고유 식별자}
	 * @return InfoSubmissionFormResponseDto {제출 양식 정보}
	 * @author sinyoung0403
	 */

	InfoSubmissionFormResponseDto findSubmissionFormById(Long clubId, Long submissionFormId);

	/**
	 * 설명: 클럽의 제출양식을 다건조회하는 메서드
	 *
	 * @param clubId   {설명: 첫 번째 매개변수 설명}
	 * @param pageable {설명: 두 번째 매개변수 설명}
	 * @return PageResponseDto<InfoSubmissionFormResponseDto> {제출 양식 정보 페이징 객체}
	 * @author sinyoung0403
	 */

	PageResponseDto<InfoSubmissionFormResponseDto> findAllSubmissionForm(Long clubId, Pageable pageable);

	/**
	 * 설명: 클럽의 제출 양식을 수정하는 메서드
	 *
	 * @param userId
	 * @param clubId
	 * @param submissionFormId                   {설명: ApplicationForm 의 고유 식별자}
	 * @param updateSubmissionTemplateRequestDto {설명: 수정할 때 필요한 정보}
	 * @throws BusinessException {404 NotFound}
	 * @author sinyoung0403
	 */

	@IsClubOwner
	void updateSubmissionTemplate(Long userId, Long clubId,
		Long submissionFormId, UpdateSubmissionTemplateRequestDto updateSubmissionTemplateRequestDto);

	/**
	 * 설명: 클럽의 제출 양식을 삭제하는 메서드
	 *
	 * <p> 양식을 삭제 시 SoftDeleted 처리 및 Club 의 JoinType 이 가입 신청 양식으로 변경
	 *
	 * @param userId           {설명: 클럽 의 고유 식별자}
	 * @param clubId           {설명: 클럽 의 고유 식별자}
	 * @param submissionFormId {설명: SubmissionForm 의 고유 식별자}
	 * @throws BusinessException {404 NotFound}
	 * @author sinyoung0403
	 */

	@IsClubOwner
	void deleteSubmissionForm(Long userId, Long clubId, Long submissionFormId);
}
