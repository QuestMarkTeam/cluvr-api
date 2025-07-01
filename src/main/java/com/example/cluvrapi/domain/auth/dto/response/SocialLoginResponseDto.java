package com.example.cluvrapi.domain.auth.dto.response;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SocialLoginResponseDto {

	private String accessToken;
	private String refreshToken;
	private String idToken;
	private List<String> missingFields;

	public static SocialLoginResponseDto of(
		String accessToken,
		String refreshToken,
		String idToken,
		List<String> missingFields
	) {
		SocialLoginResponseDto dto = new SocialLoginResponseDto();
		dto.accessToken    = accessToken;
		dto.refreshToken   = refreshToken;
		dto.idToken        = idToken;
		dto.missingFields  = missingFields;
		return dto;
	}
}
