package com.example.cluvrapi.domain.user.service;

import com.example.cluvrapi.domain.user.dto.request.LoginUserRequestDto;
import com.example.cluvrapi.domain.user.dto.request.SignUpUserRequestDto;
import com.example.cluvrapi.domain.user.dto.response.GetUserMeResponseDto;
import com.example.cluvrapi.domain.user.dto.response.GetUserOtherResponseDto;
import com.example.cluvrapi.domain.user.dto.response.GetUserPointResponseDto;
import com.example.cluvrapi.domain.user.dto.response.LoginUserResponseDto;
import com.example.cluvrapi.domain.user.dto.response.SignUpUserResponseDto;

public interface UserService {
	SignUpUserResponseDto signUp(SignUpUserRequestDto requestDto);

	LoginUserResponseDto login(LoginUserRequestDto requestDto);

	GetUserMeResponseDto getMyProfile(Long userId);

	GetUserOtherResponseDto getOtherUserProfile(Long otherUserId);

	GetUserPointResponseDto getUserPoint(Long userId);

}
