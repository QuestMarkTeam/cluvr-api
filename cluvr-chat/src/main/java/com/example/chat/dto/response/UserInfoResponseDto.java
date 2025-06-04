package com.example.chat.dto.response;

import lombok.Getter;

@Getter
public class UserInfoResponseDto {
	private Long userId;
	private String nickname;
	private String role;
	private String imageUrl;
}
