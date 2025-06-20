package com.example.cluvrapi.domain.clover.service;

import com.example.cluvrapi.domain.clover.dto.request.CreateCloverRequestDto;
import com.example.cluvrapi.domain.clover.dto.request.UpdateCloverRequestDto;
import com.example.cluvrapi.domain.clover.dto.response.FindCloverResponseDto;

public interface CloverService extends CloverLogService {
	/**
	 * 설명: 유저 ID 기반으로 등급,점수 조회
	 *
	 * @param userId 유저 ID
	 * @return 유저 등급 조회 결과
	 *
	 * @author 나원준
	 */
	public FindCloverResponseDto findCloverByUserId(Long userId);

	public void deleteClover(Long cloverId);

	/**
	 *
	 * api 요청이 아닌 스케줄러를 통해 주입
	 *
	 * @param requestDto 통계 테이블에 저장한 집계 데이터를 넣어줌
	 *
	 * @author 나원준
	 */

	public void createClover(CreateCloverRequestDto requestDto);

	public void updateClover(UpdateCloverRequestDto requestDto);
}
