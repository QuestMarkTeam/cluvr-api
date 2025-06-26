package com.example.cluvrapi.domain.gem.service;

import com.example.cluvrapi.domain.gem.dto.request.UpdateGemRequestDto;
import com.example.cluvrapi.domain.gem.dto.response.UpdateGemResponseDto;
import com.example.cluvrapi.domain.gem.enums.GemUserActivityType;

public interface GemService extends GemLogService {
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

	UpdateGemResponseDto chargeGem(Long userId, UpdateGemRequestDto requestDto);

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

	UpdateGemResponseDto useGem(Long userId, UpdateGemRequestDto requestDto);

	/**
	 * 설명: {메서드에 대한 간략한 설명을 작성합니다.}
	 *
	 * <p>{추가적인 설명이 필요하다면 여기에 작성합니다.}
	 *
	 * @param updateGemRequestDto
	 *
	 *
	 * @author 나원준
	 */

	void updateGems(UpdateGemRequestDto updateGemRequestDto);

	void earnGems(GemUserActivityType gemUserActivityType, Long userId);
}
