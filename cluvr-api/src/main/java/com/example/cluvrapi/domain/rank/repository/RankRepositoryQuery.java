package com.example.cluvrapi.domain.rank.repository;

import java.util.List;

import com.example.cluvrapi.domain.rank.dto.FindRankLogResponseDto;
import com.example.cluvrapi.domain.rank.dto.FindRankResponseDto;

public interface RankRepositoryQuery {
	/**
	 * 설명: 유저 등급 조회
	 *
	 * @param userId 유저 ID
	 * @return 유저 등급 정보
	 *
	 * @author 나원준
	 */

	public FindRankResponseDto findScoreByUserId(Long userId);

	/**
	 * 설명: 유저 등급 점수 지급 로그 조회 추후 레디스 기반도 고려 중
	 *
	 * @param userId 유저 ID
	 * @return 유저 등급 점수 지급 로그
	 * @author 나원준
	 */

	public List<FindRankLogResponseDto> findRankLogByUserId(Long userId);
}
