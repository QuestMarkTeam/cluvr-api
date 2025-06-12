package com.example.cluvrapi.domain.clover.service;

import java.util.List;

import com.example.cluvrapi.domain.clover.dto.FindCloverLogResponseDto;
import com.example.cluvrapi.domain.clover.dto.request.CreateCloverLogRequestDto;

public interface CloverLogService {
	/**
	 * 설명: 유저 ID 기반으로 등급,점수 적립,사용 로그 상세 조회
	 *
	 * @param userId 유저 ID
	 * @return 유저 등급 로그 조회 결과
	 * @author 나원준
	 */
	public List<FindCloverLogResponseDto> findCloverLogByUserId(Long userId);

	/**
	 * 설명: 등급 로그 저장
	 *
	 * @param requestDto 레디스에 저장한 로그기록 db로 옮겨줌
	 *
	 * @author 나원준
	 */

	public void createCloverLog(CreateCloverLogRequestDto requestDto);

	/**
	 * 설명: 오래된 로그 기록 삭제
	 *
	 * @param cloverId 등급 테이블 식별자
	 *
	 * @author 나원준
	 */

	public void deleteCloverLog(Long cloverId);
}
