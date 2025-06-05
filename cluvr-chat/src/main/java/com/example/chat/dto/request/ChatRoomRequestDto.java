package com.example.chat.dto.request;

import com.example.chat.enums.ClubRole;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ChatRoomRequestDto {
	private Long userId;
	private ClubRole role;

	public ChatRoomRequestDto(Long userId, ClubRole role) {
		this.userId = userId;
		this.role = role;
	}

	public static ChatRoomRequestDto from(Long userId, ClubRole role) {
		return new ChatRoomRequestDto(userId, role);
	}
}
