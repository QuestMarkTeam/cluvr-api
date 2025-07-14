package com.example.cluvrapi.domain.auth.service;

import com.example.cluvrapi.domain.auth.dto.request.LoginUserRequestDto;
import com.example.cluvrapi.domain.auth.dto.request.SignUpUserRequestDto;
import com.example.cluvrapi.domain.auth.dto.response.LoginUserResponseDto;
import com.example.cluvrapi.domain.user.entity.User;

public interface AuthService {
	void signUp(SignUpUserRequestDto requestDto);
	LoginUserResponseDto login(LoginUserRequestDto requestDto);
	void logout(String accessToken);
	// SignUpUserResponseDto testSignUp(SignUpUserRequestDto dto);
}
