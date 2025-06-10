package com.example.chat.dto.request;

import com.example.chat.enums.MessageType;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatMessageRequestDto {
	private MessageType type;
	private Long roomId;
	private Long userId;
	private String nickname;
	private String message;

	/****
	 * Constructs a new ChatMessageRequestDto with the specified message type, room ID, user ID, nickname, and message content.
	 *
	 * @param type the type of the chat message
	 * @param roomId the identifier of the chat room
	 * @param userId the identifier of the user sending the message
	 * @param nickname the nickname of the user
	 * @param message the content of the chat message
	 */
	public ChatMessageRequestDto(MessageType type, Long roomId, Long userId, String nickname, String message) {
		this.type = type;
		this.roomId = roomId;
		this.userId = userId;
		this.nickname = nickname;
		this.message = message;
	}

	/**
	 * Creates a new {@code ChatMessageRequestDto} instance with the specified message details.
	 *
	 * @param type the type of the message
	 * @param roomId the ID of the chat room
	 * @param userId the ID of the user sending the message
	 * @param nickname the nickname of the user
	 * @param message the content of the message
	 * @return a new {@code ChatMessageRequestDto} containing the provided information
	 */
	public static ChatMessageRequestDto from(MessageType type, Long roomId, Long userId, String nickname,
		String message) {
		return new ChatMessageRequestDto(type, roomId, userId, nickname, message);
	}
}
