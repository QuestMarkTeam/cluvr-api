package com.example.chat.dto.request;

import lombok.Getter;

@Getter
public class JoinRequestDto {
	private Long userId;

	public JoinRequestDto(Long userId) {
		this.userId = userId;
	}

	public static JoinRequestDto from(Long userId) {
		return new JoinRequestDto(userId);
	}
}
