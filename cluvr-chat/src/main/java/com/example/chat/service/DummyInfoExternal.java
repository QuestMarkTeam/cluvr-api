package com.example.chat.service;

import org.springframework.stereotype.Component;

import com.example.chat.dto.response.UserInfoResponseDto;

@Component
public class DummyInfoExternal implements GetInfoFromExternal {
	@Override
	public UserInfoResponseDto getUserInfo(Long userId) {
		return new UserInfoResponseDto(userId, "테스트 유저" + userId, "MEMBER", "http://example.com/profile.png");
	}
}
