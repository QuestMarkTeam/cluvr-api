package com.example.cluvrapi.domain.point.service;

import java.util.List;

import com.example.cluvrapi.domain.point.dto.response.FindPointLogResponseDto;

public interface PointLogService {
	/**
	 * 설명: 유저 포인트 지급 로그 조회 추후 레디스 기반도 고려 중
	 *
	 * @param userId 유저 ID
	 * @return 유저 포인트 지급 로그
	 *
	 * @author 나원준
	 */

	public List<FindPointLogResponseDto> findPointLogByUserId(Long userId);
}
