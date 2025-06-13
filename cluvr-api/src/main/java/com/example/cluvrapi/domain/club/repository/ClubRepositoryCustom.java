package com.example.cluvrapi.domain.club.repository;

import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.club.dto.response.FindAllClubResponseDto;
import com.example.cluvrapi.domain.club.dto.response.FindClubResponseDto;
import com.example.cluvrapi.domain.club.enums.ClubType;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;

/**
 * 클럽 관련 맞춤 쿼리 메서드를 정의하는 커스텀 리포지토리 인터페이스
 *
 * <p>기본 CRUD 이외에 클럽 단건 조회 및 다건 조회(페이징 포함) 기능을 제공합니다.
 *
 * @author sinyoung0403
 */

public interface ClubRepositoryCustom {

	/**
	 * 설명: 클럽 고유 ID로 클럽 상세 정보를 조회
	 *
	 * @param clubId 조회할 클럽의 고유 식별자
	 * @return 클럽 상세 정보를 담은 DTO
	 * @author sinyoung0403
	 */

	FindClubResponseDto findClubById(Long clubId);

	/**
	 * 설명: 클럽 타입과 페이징 정보를 기반으로 클럽 리스트를 조회합니다.
	 *
	 * @param clubType 조회할 클럽의 타입
	 * @param pageable 페이징 및 정렬 정보
	 * @return 페이징 처리된 클럽 리스트 DTO
	 * @author sinyoung0403
	 */

	PageResponseDto<FindAllClubResponseDto> findAllClub(ClubType clubType, Pageable pageable);
}
