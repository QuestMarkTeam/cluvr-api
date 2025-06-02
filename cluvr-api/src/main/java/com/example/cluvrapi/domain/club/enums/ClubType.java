package com.example.cluvrapi.domain.club.enums;

import java.util.Arrays;

import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.ResponseCode;

public enum ClubType {
	STUDY,
	PROJECT,
	COMMUNITY;

	public static ClubType of(String clubType) {
		return Arrays.stream(ClubType.values())
			.filter(r -> r.name().equalsIgnoreCase(clubType))
			.findFirst()
			.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_REQUEST, "유효하지 않은 clubType"));
	}
}
