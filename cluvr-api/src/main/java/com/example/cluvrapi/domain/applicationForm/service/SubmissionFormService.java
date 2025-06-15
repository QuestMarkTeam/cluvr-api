package com.example.cluvrapi.domain.applicationForm.service;

import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.applicationForm.dto.request.CreateSubmissionFormRequestDto;
import com.example.cluvrapi.domain.applicationForm.dto.request.UpdateSubmissionTemplateRequestDto;
import com.example.cluvrapi.domain.applicationForm.dto.response.CreateSubmissionFormResponseDto;
import com.example.cluvrapi.domain.applicationForm.dto.response.InfoSubmissionFormResponseDto;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.global.exception.BusinessException;

public interface SubmissionFormService {

	/**
		 * 클럽에 대한 새로운 제출 양식을 생성합니다.
		 *
		 * @param userId 제출 양식을 생성하는 사용자의 고유 식별자
		 * @param clubId 제출 양식을 생성할 클럽의 고유 식별자
		 * @param submissionFormRequestDto 제출 양식 생성에 필요한 정보가 담긴 DTO
		 * @return 생성된 제출 양식의 식별자가 포함된 DTO
		 * @throws BusinessException 클럽 또는 사용자를 찾을 수 없는 경우 404 예외가 발생합니다.
		 */

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
		 * 클럽의 특정 제출 양식을 수정합니다.
		 *
		 * @param submissionFormId 수정할 제출 양식의 고유 식별자
		 * @param updateSubmissionTemplateRequestDto 제출 양식 수정에 필요한 정보
		 * @throws BusinessException 클럽 또는 제출 양식을 찾을 수 없는 경우 404 예외가 발생합니다.
		 */

	void updateSubmissionTemplate(Long userId, Long clubId,
		Long submissionFormId, UpdateSubmissionTemplateRequestDto updateSubmissionTemplateRequestDto);

	/**
 * 클럽의 제출 양식을 소프트 삭제하고, 클럽의 가입 유형을 '가입 신청 양식'으로 변경합니다.
 *
 * @param userId 클럽 제출 양식 삭제를 요청한 사용자의 고유 식별자
 * @param clubId 클럽의 고유 식별자
 * @param submissionFormId 삭제할 제출 양식의 고유 식별자
 * @throws BusinessException 클럽 또는 제출 양식을 찾을 수 없는 경우 404 예외를 발생시킵니다.
 */

	void deleteSubmissionForm(Long userId, Long clubId, Long submissionFormId);
}
