package com.example.cluvrapi.domain.user.service;

import com.example.cluvrapi.domain.user.dto.request.UpdateUserRequestDto;
import com.example.cluvrapi.domain.user.dto.response.GetUserMeResponseDto;
import com.example.cluvrapi.domain.user.dto.response.GetUserOtherResponseDto;
import com.example.cluvrapi.domain.user.dto.response.GetUserPointResponseDto;

public interface UserService {

	GetUserMeResponseDto getMyProfile(Long userId);

	GetUserOtherResponseDto getOtherUserProfile(Long otherUserId);

	GetUserPointResponseDto getUserPoint(Long userId);

	GetUserMeResponseDto updateMyProfile(Long userId, UpdateUserRequestDto updateDto);

	void deleteMyProfile(Long userId);

}
