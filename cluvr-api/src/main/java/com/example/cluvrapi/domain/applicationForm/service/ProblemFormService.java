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
	 * 설명: 클럽의 문제양식을 생성하는 메서드
	 *
	 * @param clubId                      {설명: 클럽의 고유 식별자}
	 * @param createProblemFormRequestDto {설명: 문제 양식에 필요한 정보}
	 * @return CreateProblemFormResponseDto {설명: 클럽의 id 값 반환}
	 * @throws BusinessException {404 NotFound}
	 * @author sinyoung0403
	 */

	CreateProblemFormResponseDto createProblemForm(Long clubId,
		CreateProblemFormRequestDto createProblemFormRequestDto);

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

	PageResponseDto<InfoProblemFormResponseDto> findAllProblemForm(Long clubId, Pageable pageable);

	/**
	 * 설명: 클럽의 문제양식을 수정하는 메서드
	 *
	 * @param clubId                      {설명: 클럽의 고유 식별자}
	 * @param problemFormId               {설명: problemForm 의 고유 식별자}
	 * @param updateProblemFormRequestDto {설명: 수정할 때 필요한 정보}
	 * @throws BusinessException {404 NotFound}
	 * @author sinyoung0403
	 */

	void updateProblemForm(Long clubId, Long problemFormId,
		UpdateProblemFormRequestDto updateProblemFormRequestDto);

	/**
	 * 설명: 클럽의 문제양식을 삭제하는 메서드
	 *
	 * <p> 양식을 삭제 시 SoftDeleted 처리 및 Club 의 JoinType 이 가입 신청 양식으로 변경
	 *
	 * @param clubId        {설명: 클럽의 고유 식별자}
	 * @param problemFormId {설명: problemForm 의 고유 식별자}
	 * @throws BusinessException {404 NotFound}
	 * @author sinyoung0403
	 */

	void deleteProblem(Long clubId, Long problemFormId);
}
