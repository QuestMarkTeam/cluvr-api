package com.example.cluvrapi.domain.til.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.til.dto.response.InfoTilResponseDto;
import com.example.cluvrapi.domain.til.entity.Til;

public interface TilRepositoryQuery {

	/**
	 * 설명: Til 를 단건 조회하는 쿼리문
	 *
	 * <p> Dto Projection 적용
	 *
	 * @param tilId {설명: Til 고유 식별자}
	 * @return InfoTilResponseDto {설명: Til 정보}
	 * @author sinoyoung0403
	 */

	InfoTilResponseDto findTilById(Long tilId);

	/**
	 * 설명: Til 를 다건 조회하는 쿼리문
	 *
	 * <p> Dto Projection 적용, 페이징 처리
	 *
	 * @param clubId   {설명: 클럽 고유 식별자}
	 * @param pageable {설명: 페이징 객체}
	 * @return PageResponseDto<InfoTilResponseDto> {설명: Til 정보 페이징 객체}
	 * @author {sinyoung0403}
	 */

	PageResponseDto<InfoTilResponseDto> findAllTilById(Long clubId, Pageable pageable);

	/**
	 * 설명: Til 단건 조회하는 쿼리문
	 *
	 * <p> clubId와 tilId를 이용한 조회 처리
	 *
	 * @param clubId {설명: 클럽 고유 식별자}
	 * @param tilId  {설명: Til 고유 식별자}
	 * @return Optional<Til> {설명: Til 객체 (존재하지 않을 수 있음)}
	 * @author {sinyoung0403}
	 */
	Optional<Til> findTilByIdAndClubId(Long clubId, Long tilId);
}
