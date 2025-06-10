package com.example.chat.dto.response;

import lombok.Getter;

@Getter
public class UserInfoResponseDto {
	private Long userId;
	private String nickname;
	private String role;
	private String imageUrl;

	/**
	 * Constructs a UserInfoResponseDto with the specified user ID, nickname, role, and image URL.
	 *
	 * @param userId   the unique identifier of the user
	 * @param nickname the user's display name
	 * @param role     the user's role
	 * @param imageUrl the URL of the user's profile image
	 */
	public UserInfoResponseDto(Long userId, String nickname, String role, String imageUrl) {
		this.userId = userId;
		this.nickname = nickname;
		this.role = role;
		this.imageUrl = imageUrl;
	}

	/**
	 * Creates a new UserInfoResponseDto instance with the specified user information.
	 *
	 * @param userId the unique identifier of the user
	 * @param nickname the user's nickname
	 * @param role the user's role
	 * @param imageUrl the URL of the user's profile image
	 * @return a UserInfoResponseDto containing the provided user details
	 */
	public static UserInfoResponseDto from(Long userId, String nickname, String role, String imageUrl) {
		return new UserInfoResponseDto(userId, nickname, role, imageUrl);
	}
}
