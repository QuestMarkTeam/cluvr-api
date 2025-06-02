package com.example.chat.entity;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Getter;

@Getter
@Document(collation = "chat_log")
public class ChatLog {

	@Id
	private String id; // MongoDB에서 id는 String 값

	private Long roomId;
	private Long userId;

	@Lob
	private String message;

	private LocalDateTime createdAt;

	public ChatLog(Long roomId, Long userId, String message, LocalDateTime createdAt) {
		this.roomId = roomId;
		this.userId = userId;
		this.message = message;
		this.createdAt = createdAt;
	}
}
