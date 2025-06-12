package com.example.chat.kafka;

import java.time.LocalDateTime;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.example.chat.dto.request.ChatMessageRequestDto;
import com.example.chat.entity.ChatLog;
import com.example.chat.repository.ChatLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaChatConsumer {
	private final ChatLogRepository chatLogRepository;
	private final ObjectMapper objectMapper;
	private final SimpMessagingTemplate messagingTemplate;

	// Kafka 토픽에서 메세지를 읽기
	// 이 메세지를 MongoDB로 저장
	// @KafkaListener 스프링이 자동으로 kafka에서 메세지가 push 해 줄 때 실행 시켜줌
	@KafkaListener(topics = "chat-log", groupId = "chat-group")
	public void consume(String message) throws JsonProcessingException {
		ChatMessageRequestDto dto = objectMapper.readValue(message, ChatMessageRequestDto.class);
		ChatLog log = new ChatLog(dto.getRoomId(), dto.getUserId(), dto.getNickname(), dto.getMessage(), dto.getType(),
			LocalDateTime.now());
		System.out.println("🥕🥕🥕 Kafka 서버에서 MongoDB로 채팅 로그 저장");
		chatLogRepository.save(log);
		messagingTemplate.convertAndSend("/sub/chat/room/" + dto.getRoomId(), dto);
	}
}
