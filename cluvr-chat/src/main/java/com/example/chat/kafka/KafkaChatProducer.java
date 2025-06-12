package com.example.chat.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaChatProducer {
	// KafkaTemplate<K, V>는 Spring Kafka가 제공하는 클래스,
	// Kafka 브로커로 메시지를 보내기 위한 핵심 클래스.
	private final KafkaTemplate<String, String> kafkaTemplate;

	public void sendMessage(String topic, String message) {
		// "chat-log"라는 Kafka 토픽(topic)에
		// → message라는 문자열 메시지를 전송(Publish)
		System.out.println("🥕🥕🥕 Kafka 서버에서 메세지 받음");
		kafkaTemplate.send(topic, message);
	}
}
