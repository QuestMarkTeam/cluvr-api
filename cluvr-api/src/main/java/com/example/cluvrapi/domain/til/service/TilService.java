package com.example.cluvrapi.domain.til.service;

import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.til.dto.reqeust.CreateTilRequestDto;
import com.example.cluvrapi.domain.til.dto.reqeust.UpdateTilRequestDto;
import com.example.cluvrapi.domain.til.dto.response.CreateTilResponseDto;
import com.example.cluvrapi.domain.til.dto.response.InfoTilResponseDto;
import com.example.cluvrapi.global.exception.BusinessException;

public interface TilService {

	/**
	 * 설명: Til 생성하는 메서드
	 *
	 * @param userId              {설명: 유저 고유 식별자}
	 * @param clubId              {설명: 클럽 고유 식별자}
	 * @param createTilRequestDto {설명: 클럽 생성시 필요한 정보}
	 * @return CreateTilResponseDto {클럽 id 반환}
	 * @throws BusinessException {404 NotFound}
	 * @author {sinyoung0403}
	 */

	CreateTilResponseDto createTil(Long userId, Long clubId, CreateTilRequestDto createTilRequestDto);

	/**
	 * 설명: Til 단건 조회하는 메서드
	 *
	 * @param tilId {설명: Til 고유 식별자}
	 * @return InfoTilResponseDto {클럽 정보}
	 * @author {sinyoung0403}
	 */

	InfoTilResponseDto findTilById(Long tilId);

	/**
	 * 설명: Til 다건 조회하는 메서드
	 *
	 * @param clubId   {설명: 클럽 고유 식별자}
	 * @param pageable {설명: 페이징 객체}
	 * @return PageResponseDto<InfoTilResponseDto> {설명: Til 정보 페이징 객체}
	 * @author {sinyoung0403}
	 */

	PageResponseDto<InfoTilResponseDto> findAllTilById(Long clubId, Pageable pageable);

	/**
	 * 설명: Til 업데이트하는 메서드
	 *
	 * <p> 값이 없을 시, 업데이트하지 않는다.
	 *
	 * @param userId              {설명: 유저 고유 식별자}
	 * @param tilId               {설명: Til 고유 식별자}
	 * @param updateTilRequestDto {설명: 클럽 업데이트시 필요한 정보}
	 * @return
	 * @throws BusinessException {404 NotFound}
	 * @author {sinyoung0403}
	 */

	void updateTil(Long userId, Long tilId, UpdateTilRequestDto updateTilRequestDto);

	/**
	 * 설명: Til 삭제하는 메서드
	 *
	 * @param userId {설명: 유저 고유 식별자}
	 * @param tilId  {설명: Til 고유 식별자}
	 * @return
	 * @throws BusinessException {404 NotFound}
	 * @author {sinyoung0403}
	 */
	void deleteTil(Long userId, Long clubId, Long tilId);
}
