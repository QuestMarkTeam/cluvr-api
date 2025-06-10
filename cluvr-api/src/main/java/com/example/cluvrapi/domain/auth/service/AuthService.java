package com.example.cluvrapi.domain.auth.service;

import com.example.cluvrapi.domain.auth.dto.request.LoginUserRequestDto;
import com.example.cluvrapi.domain.auth.dto.request.SignUpUserRequestDto;
import com.example.cluvrapi.domain.auth.dto.response.LoginUserResponseDto;
import com.example.cluvrapi.domain.auth.dto.response.SignUpUserResponseDto;

public interface AuthService {
	SignUpUserResponseDto signUp(SignUpUserRequestDto requestDto);

	LoginUserResponseDto login(LoginUserRequestDto requestDto);

	void logout(Long userId, String accessToken);

}
