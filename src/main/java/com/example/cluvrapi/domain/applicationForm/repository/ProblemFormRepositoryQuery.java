package com.example.cluvrapi.domain.applicationForm.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.applicationForm.dto.response.InfoProblemFormResponseDto;
import com.example.cluvrapi.domain.applicationForm.entity.ProblemForm;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;

public interface ProblemFormRepositoryQuery {

	/**
	 * 설명: 클럽 정보와 문제 양식 정보가 일치하는 ProblemForm 객체를 반환하는 쿼리문
	 *
	 * @param clubId    {설명: 클럽 고유 식별자}
	 * @param problemId {설명: 문제 양식 고유 식별자}
	 * @return Optional<ProblemForm> {ProblemForm Optional 객체}
	 * @author sinyoung0403
	 */

	Optional<ProblemForm> findByClubIdAndProblemFormId(Long clubId, Long problemId);

	/**
	 * 설명: 문제양식을 단건 조회하는 쿼리문
	 *
	 * <p> Dto Projection 적용
	 *
	 * @param clubId    {설명: 클럽 고유 식별자}
	 * @param problemId {설명: 문제 양식 고유 식별자}
	 * @return Optional<InfoProblemFormResponseDto> {InfoProblemFormResponseDto Optional 객체}
	 * @author sinyoung0403
	 */

	Optional<InfoProblemFormResponseDto> findProblemFormById(Long clubId, Long problemId);

	/**
	 * 설명: 문제 양식을 다건 조회하는 쿼리문
	 *
	 * <p> Dto Projection 적용, 페이징 처리
	 *
	 * @param clubId   {설명: 클럽 고유 식별자}
	 * @param pageable {설명: 페이징 객체}
	 * @return PageResponseDto<InfoProblemFormResponseDto> {문제 양식 정보 페이징 객체}
	 * @author sinyoung0403
	 */

	PageResponseDto<InfoProblemFormResponseDto> findByProblemFormAllById(Long clubId, Pageable pageable);

	/**
	 * 설명: 클럽의 활성화된 문제양식 ID를 조회하는 메서드
	 *
	 * <p> SoftDelete가 적용된 데이터 제외, 단건 조회
	 *
	 * @param clubId {설명: 클럽 고유 식별자}
	 * @return Optional<ProblemForm> {설명: 활성화된 문제양식 (없을 경우 빈 Optional)}
	 * @author sinyoung0403
	 */

	Optional<ProblemForm> findActiveProblemFormByClubId(Long clubId);
}
