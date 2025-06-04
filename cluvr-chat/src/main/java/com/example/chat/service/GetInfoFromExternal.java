package com.example.chat.service;

import com.example.chat.dto.response.UserInfoResponseDto;

public interface GetInfoFromExternal {
	UserInfoResponseDto getUserInfo(Long userId);
}
