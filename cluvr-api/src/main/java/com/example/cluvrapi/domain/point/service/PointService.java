package com.example.cluvrapi.domain.point.service;

import jakarta.validation.Valid;

import com.example.cluvrapi.domain.point.dto.request.CreatePointRequestDto;
import com.example.cluvrapi.domain.point.dto.response.CreatePointResponseDto;

public interface PointService extends PointLogService {
	/**
	 * 설명: {메서드에 대한 간략한 설명을 작성합니다.}
	 *
	 * <p>{추가적인 설명이 필요하다면 여기에 작성합니다.}
	 *
	 * @param userId 유저 식별자
	 * @param requestDto 포인트 충전 dto : amount 담고 있음
	 * @return 충전 된 결과
	 *
	 * @author 나원준
	 */

	CreatePointResponseDto chargePoint(Long userId, @Valid CreatePointRequestDto requestDto);
}
