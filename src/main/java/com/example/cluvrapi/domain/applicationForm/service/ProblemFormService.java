package com.example.cluvrapi.domain.applicationForm.service;

import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.applicationForm.dto.request.CreateProblemFormRequestDto;
import com.example.cluvrapi.domain.applicationForm.dto.request.UpdateProblemFormRequestDto;
import com.example.cluvrapi.domain.applicationForm.dto.response.CreateProblemFormResponseDto;
import com.example.cluvrapi.domain.applicationForm.dto.response.InfoProblemFormResponseDto;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.global.annotation.IsClubAdmin;
import com.example.cluvrapi.global.annotation.IsClubOwner;
import com.example.cluvrapi.global.exception.BusinessException;

public interface ProblemFormService {

	/**
	 * 설명: 클럽의 문제양식을 생성하는 메서드
	 *
	 * @param userId                      {설명: 유저의 고유 식별자}
	 * @param clubId                      {설명: 클럽의 고유 식별자}
	 * @param createProblemFormRequestDto {설명: 문제 양식에 필요한 정보}
	 * @return CreateProblemFormResponseDto {설명: 클럽의 id 값 반환}
	 * @throws BusinessException {404 NotFound}
	 * @author sinyoung0403
	 */

	@IsClubAdmin
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
	 * 설명: 클럽의 문제양식을 다건조회하는 메서드
	 *
	 * <p>{추가적인 설명이 필요하다면 여기에 작성합니다.}
	 *
	 * @param clubId   {설명: 클럽 고유 식별자}
	 * @param pageable {설명: 페이징 객체}
	 * @return PageResponseDto<InfoProblemFormResponseDto> {문제 양식 정보 페이징 객체}
	 * @author sinyoung0403
	 */

	@IsClubOwner
	PageResponseDto<InfoProblemFormResponseDto> findAllProblemForm(Long clubId, Pageable pageable);

	/**
	 * 설명: 클럽의 활성화된 문제양식을 조회하는 메서드
	 *
	 * @param clubId {설명: 클럽의 고유 식별자}
	 * @return InfoProblemFormResponseDto {설명: 활성 문제양식 응답 DTO}
	 * @throws BusinessException {404 NotFound} 활성 문제양식이 없을 경우
	 * @author sinyoung0403
	 */

	InfoProblemFormResponseDto findActiveProblemFormByClubId(Long clubId);

	/**
	 * 설명: 클럽의 문제양식을 수정하는 메서드
	 *
	 * @param userId                      {설명: 유저의 고유 식별자}
	 * @param clubId                      {설명: 클럽의 고유 식별자}
	 * @param problemFormId               {설명: problemForm 의 고유 식별자}
	 * @param updateProblemFormRequestDto {설명: 수정할 때 필요한 정보}
	 * @throws BusinessException {404 NotFound}
	 * @author sinyoung0403
	 */

	@IsClubOwner
	void updateProblemForm(Long userId, Long clubId,
		Long problemFormId, UpdateProblemFormRequestDto updateProblemFormRequestDto);

	/**
	 * 설명: 클럽의 문제양식을 삭제하는 메서드
	 *
	 * <p> 양식을 삭제 시 SoftDeleted 처리 및 Club 의 JoinType 이 가입 신청 양식으로 변경
	 *
	 * @param userId        {설명: 유저의 고유 식별자}
	 * @param clubId        {설명: 클럽의 고유 식별자}
	 * @param problemFormId {설명: problemForm 의 고유 식별자}
	 * @throws BusinessException {404 NotFound}
	 * @author sinyoung0403
	 */

	@IsClubOwner
	void deleteProblem(Long userId, Long clubId, Long problemFormId);

	/**
	 * 설명: 클럽 문제양식의 활성화 상태를 변경하는 메서드
	 *
	 * @param id            {설명: 요청자(사용자)의 고유 식별자}
	 * @param clubId        {설명: 클럽의 고유 식별자}
	 * @param problemFormId {설명: 문제양식의 고유 식별자}
	 * @param active        {설명: 활성화 여부 (true: 활성화, false: 비활성화)}
	 * @throws BusinessException {404 NotFound} 문제양식 또는 클럽을 찾지 못했을 경우
	 * @author sinyoung0403
	 */

	@IsClubOwner
	void changeActivationState(Long id, Long clubId, Long problemFormId, Boolean active);
}
