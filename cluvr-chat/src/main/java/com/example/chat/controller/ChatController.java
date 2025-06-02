package com.example.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.chat.dto.request.ChatMessageDto;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {

	private final SimpMessagingTemplate messagingTemplate;

	@MessageMapping("/message") // /pub/chat/message
	public void sendMessage(@Payload ChatMessageDto chatMessageDto) {
		String roomId = "/sub/chat/room/" + chatMessageDto.getRoomId();
		messagingTemplate.convertAndSend(roomId, chatMessageDto);
	}
}
