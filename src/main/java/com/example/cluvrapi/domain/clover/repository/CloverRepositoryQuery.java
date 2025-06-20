package com.example.cluvrapi.domain.clover.repository;

import java.util.List;

import com.example.cluvrapi.domain.clover.dto.response.FindCloverLogResponseDto;
import com.example.cluvrapi.domain.clover.dto.response.FindCloverResponseDto;

public interface CloverRepositoryQuery {
	/**
	 * 설명: 유저 등급 조회
	 *
	 * @param userId 유저 ID
	 * @return 유저 등급 정보
	 *
	 * @author 나원준
	 */

	public FindCloverResponseDto findScoreByUserId(Long userId);

	/**
	 * 설명: 유저 등급 점수 지급 로그 조회 추후 레디스 기반도 고려 중
	 *
	 * @param userId 유저 ID
	 * @return 유저 등급 점수 지급 로그
	 * @author 나원준
	 */

	public List<FindCloverLogResponseDto> findCloverLogByUserId(Long userId);
}
