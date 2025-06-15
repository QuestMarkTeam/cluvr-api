package com.example.cluvrapi.domain.applicationForm.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.applicationForm.dto.response.InfoProblemFormResponseDto;
import com.example.cluvrapi.domain.applicationForm.entity.ProblemForm;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;

public interface ProblemFormRepositoryCustom {

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
	 * @return InfoProblemFormResponseDto {문제 양식 정보}
	 * @author sinyoung0403
	 */

	InfoProblemFormResponseDto findProblemFormById(Long clubId, Long problemId);

	/****
 * 특정 클럽의 모든 문제 양식 정보를 페이징하여 조회합니다.
 *
 * DTO 프로젝션을 사용하여 문제 양식 정보를 반환하며, 페이징 처리가 적용됩니다.
 *
 * @param clubId 조회할 클럽의 고유 식별자
 * @param pageable 페이징 및 정렬 정보를 담은 객체
 * @return 문제 양식 정보의 페이징 결과
 */

	PageResponseDto<InfoProblemFormResponseDto> findByProblemFormAllById(Long clubId, Pageable pageable);

	/**
 * 주어진 클럽 ID에 해당하는 활성화된 문제양식 엔티티를 조회합니다.
 *
 * SoftDelete가 적용되지 않은(삭제되지 않은) 문제양식 중 하나를 Optional로 반환합니다.
 *
 * @param clubId 클럽의 고유 식별자
 * @return 활성화된 문제양식 엔티티가 존재하면 Optional로 반환하며, 없으면 빈 Optional을 반환합니다.
 */

	Optional<ProblemForm> findActiveProblemFormByClubId(Long clubId);
}
