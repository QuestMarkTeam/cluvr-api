package com.example.cluvrapi.domain.gem.repository;

import java.util.List;

import com.example.cluvrapi.domain.gem.dto.response.FindGemLogResponseDto;

public interface GemLogRepositoryQuery {

	/**
	 * 설명: 유저 포인트 지급 로그 조회 추후 레디스 기반도 고려 중
	 *
	 * @param userId 유저 ID
	 * @return 유저 포인트 지급 로그
	 *
	 * @author 나원준
	 */

	public List<FindGemLogResponseDto> findGemLogByUserId(Long userId);
}
