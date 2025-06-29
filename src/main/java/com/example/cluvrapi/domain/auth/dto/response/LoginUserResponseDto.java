package com.example.cluvrapi.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class LoginUserResponseDto {
	private final String name;
	private final String email;
	private final String accessToken;
	private final String refreshToken;
	private final String idToken;

	public LoginUserResponseDto(String name, String email, String accessToken, String refreshToken,
		String idToken) {
		this.name = name;
		this.email = email;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.idToken = idToken;
	}

	/**
	 * User 엔티티와 생성된 토큰 두 개를 받아서 DTO를 생성합니다.
	 */
	public static LoginUserResponseDto from(String email, String name, String accessToken, String refreshToken,
		String idToken) {
		return new LoginUserResponseDto(name, email, accessToken, refreshToken,
			idToken);
	}
}
