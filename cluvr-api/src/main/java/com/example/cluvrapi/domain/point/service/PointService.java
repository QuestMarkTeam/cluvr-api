package com.example.cluvrapi.domain.point.service;

import com.example.cluvrapi.domain.point.dto.request.UpdatePointRequestDto;
import com.example.cluvrapi.domain.point.dto.response.UpdatePointResponseDto;
import com.example.cluvrapi.domain.point.entity.PointType;

public interface PointService extends PointLogService {
	/**
	 * 설명: 포인트 충전 메소드
	 *
	 * 결제 시스템 고려 중
	 *
	 * @param userId 유저 식별자
	 * @param requestDto 포인트 충전 dto : amount 담고 있음
	 * @return 충전 된 결과
	 *
	 * @author 나원준
	 */

	UpdatePointResponseDto chargePoint(Long userId, UpdatePointRequestDto requestDto);

	/**
	 * 설명: 포인트 차감 메소드
	 *
	 *
	 * @param userId 유저 식별자
	 * @param requestDto 포인트 차감 dto : amount 담고 있음
	 * @return {반환값에 대한 설명}
	 * @throws  {포인트 부족하면 400에러 }
	 *
	 * @author 나원준
	 */

	UpdatePointResponseDto usePoint(Long userId, UpdatePointRequestDto requestDto);

	/**
	 * 설명: {메서드에 대한 간략한 설명을 작성합니다.}
	 *
	 * <p>{추가적인 설명이 필요하다면 여기에 작성합니다.}
	 *
	 * @param userId 유저 식별자
	 * @param pointType 포인트 적립 amount
	 *
	 * @author 나원준
	 */

	void earnPoints(Long userId, PointType pointType);
}
