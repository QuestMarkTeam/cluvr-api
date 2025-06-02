package com.example.cluvrapi.domain.rank.service;

import com.example.cluvrapi.domain.rank.dto.FindRankLogResponseDto;
import com.example.cluvrapi.domain.rank.dto.request.CreateRankLogRequestDto;

public interface RankLogService {
	/**
	 * 설명: 유저 ID 기반으로 등급,점수 적립,사용 로그 상세 조회
	 *
	 * @param userId 유저 ID
	 * @return 유저 등급 로그 조회 결과
	 *
	 * @author 나원준
	 */
	public FindRankLogResponseDto findRankLogByUserId(Long userId);

	/**
	 * 설명: 등급 로그 저장
	 *
	 * @param requestDto 레디스에 저장한 로그기록 db로 옮겨줌
	 *
	 * @author 나원준
	 */

	public void createRankLog(CreateRankLogRequestDto requestDto);

	/**
	 * 설명: 오래된 로그 기록 삭제
	 *
	 * @param rankId 등급 테이블 식별자
	 *
	 * @author 나원준
	 */

	public void deleteRankLog(Long rankId);
}
