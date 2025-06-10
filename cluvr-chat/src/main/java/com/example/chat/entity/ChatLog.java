package com.example.chat.entity;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import com.example.chat.enums.MessageType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Document("chat_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatLog {
	@Id
	private String id; // MongoDB에서 id는 String 값

	private Long roomId;
	private Long userId;
	private String nickname;

	@Lob
	private String message;

	@Enumerated(EnumType.STRING)
	private MessageType type; // ENTER, TALK, LEAVE

	private LocalDateTime createdAt;

	/**
	 * Constructs a new chat log entry with the specified room, user, nickname, message content, message type, and creation timestamp.
	 *
	 * @param roomId the identifier of the chat room
	 * @param userId the identifier of the user who sent the message
	 * @param nickname the display name of the user
	 * @param message the content of the chat message
	 * @param type the type of the message (e.g., ENTER, TALK, LEAVE)
	 * @param createdAt the timestamp when the message was created
	 */
	public ChatLog(Long roomId, Long userId, String nickname, String message, MessageType type,
		LocalDateTime createdAt) {
		this.roomId = roomId;
		this.userId = userId;
		this.nickname = nickname;
		this.message = message;
		this.type = type;
		this.createdAt = createdAt;
	}
}
