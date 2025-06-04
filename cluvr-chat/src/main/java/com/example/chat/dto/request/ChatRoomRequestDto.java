package com.example.chat.dto.request;

import com.example.chat.enums.ClubRole;

import lombok.Getter;

@Getter
public class ChatRoomRequestDto {
	private Long userId;
	private ClubRole role;
}
