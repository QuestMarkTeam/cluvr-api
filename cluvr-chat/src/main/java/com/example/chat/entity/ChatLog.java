package com.example.chat.entity;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import com.example.chat.enums.MessageType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Document(collation = "chat_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatLog {
	@Id
	private String id; // MongoDB에서 id는 String 값

	private Long roomId;
	private Long userId;

	@Lob
	private String message;

	@Enumerated(EnumType.STRING)
	private MessageType type; // ENTER, TALK, LEAVE

	private LocalDateTime createdAt;

	public ChatLog(Long roomId, Long userId, String message, MessageType type, LocalDateTime createdAt) {
		this.roomId = roomId;
		this.userId = userId;
		this.type = type;
		this.message = message;
		this.createdAt = createdAt;
	}
}
