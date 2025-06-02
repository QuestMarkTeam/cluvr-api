package com.example.chat.dto.request;

import com.example.chat.enums.MessageType;

import lombok.Getter;

@Getter
public class ChatMessageDto {
	private MessageType type;
	private Long roomId;
	private Long userId;
	private String message;
}
