package com.example.notification.event;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.example.global.config.RabbitConfig;

@Component
@RequiredArgsConstructor
public class ChatNotificationProducer {
	private final RabbitTemplate rabbitTemplate;

	public void send(ChatNotificationEvent dto) {
		rabbitTemplate.convertAndSend(
			RabbitConfig.EXCHANGE_NAME,           // 교환기 이름
			"user." + dto.getReceiverId(),     // 라우팅 키 - user별 큐
			dto                                 // 메시지 본문
		);
	}
}
