package com.example.cluvrapi.domain.auth.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class SignUpVerifyCacheRequestDto {
	private final SignUpUserRequestDto signUpRequest;
	private final String verificationCode;

	public SignUpVerifyCacheRequestDto(SignUpUserRequestDto signUpRequest, String verificationCode) {
		this.signUpRequest = signUpRequest;
		this.verificationCode = verificationCode;
	}
}
