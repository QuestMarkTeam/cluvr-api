package com.example.cluvrapi.domain.applicationForm.service;

import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.applicationForm.dto.request.CreateProblemFormRequestDto;
import com.example.cluvrapi.domain.applicationForm.dto.request.UpdateProblemFormRequestDto;
import com.example.cluvrapi.domain.applicationForm.dto.response.CreateProblemFormResponseDto;
import com.example.cluvrapi.domain.applicationForm.dto.response.InfoProblemFormResponseDto;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.global.exception.BusinessException;

public interface ProblemFormService {

	/**
		 * 클럽에 새로운 문제 양식을 생성합니다.
		 *
		 * @param userId 문제 양식을 생성하는 사용자의 ID
		 * @param clubId 문제 양식을 생성할 클럽의 ID
		 * @param createProblemFormRequestDto 생성할 문제 양식의 정보
		 * @return 생성된 문제 양식의 클럽 ID를 포함한 응답 객체
		 * @throws BusinessException 클럽 또는 사용자를 찾을 수 없는 경우 발생
		 */

	CreateProblemFormResponseDto createProblemForm(Long userId,
		Long clubId, CreateProblemFormRequestDto createProblemFormRequestDto);

	/**
	 * 설명: 클럽의 문제양식을 단건조회하는 메서드
	 *
	 * @param clubId    {설명: 클럽 고유 식별자}
	 * @param problemId {설명: 문제 양식 고유 식별자}
	 * @return InfoProblemFormResponseDto {문제 양식 정보}
	 * @author sinyoung0403
	 */

	InfoProblemFormResponseDto findProblemFormById(Long clubId, Long problemId);

	/**
 * 특정 클럽의 모든 문제 양식을 페이징하여 조회합니다.
 *
 * @param clubId 클럽의 고유 식별자
 * @param pageable 페이징 정보를 담은 객체
 * @return 문제 양식 정보가 담긴 페이징 응답 객체
 */

	PageResponseDto<InfoProblemFormResponseDto> findAllProblemForm(Long clubId, Pageable pageable);

	/**
 * 특정 클럽의 현재 활성화된 문제양식을 조회합니다.
 *
 * @param clubId 클럽의 고유 식별자
 * @return 활성화된 문제양식 정보가 담긴 InfoProblemFormResponseDto
 * @throws BusinessException 활성화된 문제양식이 존재하지 않을 경우 발생합니다.
 */

	InfoProblemFormResponseDto findActiveProblemFormByClubId(Long clubId);

	/**
		 * 클럽의 특정 문제양식을 수정합니다.
		 *
		 * @param problemFormId 수정할 문제양식의 고유 식별자
		 * @param updateProblemFormRequestDto 문제양식 수정에 필요한 정보
		 * @throws BusinessException 해당 문제양식 또는 클럽을 찾을 수 없는 경우 발생합니다.
		 */

	void updateProblemForm(Long userId, Long clubId,
		Long problemFormId, UpdateProblemFormRequestDto updateProblemFormRequestDto);

	/**
 * 클럽의 문제 양식을 소프트 삭제하고 클럽의 가입 방식을 신청 양식으로 변경합니다.
 *
 * @throws BusinessException 해당 문제 양식이나 클럽을 찾을 수 없는 경우 발생합니다.
 */

	void deleteProblem(Long userId, Long clubId, Long problemFormId);

	/**
 * 클럽의 특정 문제양식의 활성화 상태를 변경합니다.
 *
 * @param problemFormId 활성화 상태를 변경할 문제양식의 고유 식별자
 * @param active 활성화 여부 (true: 활성화, false: 비활성화)
 * @throws BusinessException 문제양식 또는 클럽이 존재하지 않을 경우 발생합니다.
 */

	void changeActivationState(Long id, Long clubId, Long problemFormId, Boolean active);
}
