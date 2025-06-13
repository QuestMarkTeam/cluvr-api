package com.example.cluvrapi.domain.club.enums;

import java.util.Arrays;

import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.ResponseCode;

/**
 * 클럽 타입을 나타내는 열거형
 *
 * <p>클럽의 분류를 정의하며, 스터디(STUDY), 프로젝트(PROJECT), 커뮤니티(COMMUNITY)로 구분됩니다.
 *
 * @author sinyoung0403
 */

public enum ClubType {
	/**
	 * 스터디 클럽
	 */
	STUDY,
	/**
	 * 프로젝트 클럽
	 */
	PROJECT,
	/**
	 * 커뮤니티 클럽
	 */
	COMMUNITY;

	/**
	 * 설명: 문자열을 받아 해당하는 ClubType enum을 반환합니다.
	 *
	 * @param clubType 변환할 클럽 타입 문자열 (대소문자 구분 없음)
	 * @return 일치하는 ClubType enum
	 * @throws BusinessException 클럽 타입이 유효하지 않을 경우 발생
	 */

	public static ClubType of(String clubType) {
		return Arrays.stream(ClubType.values())
			.filter(r -> r.name().equalsIgnoreCase(clubType))
			.findFirst()
			.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_REQUEST, "유효하지 않은 clubType"));
	}
}
