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

	/**
		 * Constructs a ChatRoomRequestDto with the specified user ID and club role.
		 *
		 * @param userId the ID of the user
		 * @param role the role of the user in the club
		 */
	public ChatRoomRequestDto(Long userId, ClubRole role) {
		this.userId = userId;
		this.role = role;
	}

	/****
	 * Creates a new ChatRoomRequestDto instance with the specified user ID and role.
	 *
	 * @param userId the ID of the user to associate with the chat room
	 * @param role the role of the user in the chat room
	 * @return a new ChatRoomRequestDto initialized with the given user ID and role
	 */
	public static ChatRoomRequestDto from(Long userId, ClubRole role) {
		return new ChatRoomRequestDto(userId, role);
	}
}
