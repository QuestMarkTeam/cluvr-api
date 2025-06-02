package com.example.cluvrapi.domain.rank.service;

import com.example.cluvrapi.domain.rank.dto.FindRankResponseDto;
import com.example.cluvrapi.domain.rank.dto.request.CreateRankRequestDto;
import com.example.cluvrapi.domain.rank.dto.request.UpdateRankRequestDto;

public interface RankService extends RankLogService {
	/**
	 * 설명: 유저 ID 기반으로 등급,점수 조회
	 *
	 * @param userId 유저 ID
	 * @return 유저 등급 조회 결과
	 *
	 * @author 나원준
	 */
	public FindRankResponseDto findRankByUserId(Long userId);

	public void deleteRank(Long rankId);

	/**
	 *
	 * api 요청이 아닌 스케줄러를 통해 주입
	 *
	 * @param requestDto 통계 테이블에 저장한 집계 데이터를 넣어줌
	 *
	 * @author 나원준
	 */

	public void createRank(CreateRankRequestDto requestDto);

	public void updateRank(Long rankId, UpdateRankRequestDto requestDto);
}
