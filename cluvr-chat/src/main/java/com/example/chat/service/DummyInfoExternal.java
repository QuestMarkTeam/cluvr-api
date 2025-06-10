package com.example.chat.service;

import org.springframework.stereotype.Component;

import com.example.chat.dto.response.UserInfoResponseDto;

@Component
public class DummyInfoExternal implements GetInfoFromExternal {
	/**
	 * Returns dummy user information for the specified user ID.
	 *
	 * @param userId the ID of the user to retrieve information for
	 * @return a UserInfoResponseDto containing the user ID, a generated username, a fixed role, and a fixed profile image URL
	 */
	@Override
	public UserInfoResponseDto getUserInfo(Long userId) {
		return new UserInfoResponseDto(userId, "테스트 유저" + userId, "LEADER", "http://example.com/profile.png");
	}
}
