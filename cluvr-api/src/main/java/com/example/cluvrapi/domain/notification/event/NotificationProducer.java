package com.example.cluvrapi.domain.notification.event;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationProducer {

	private final RabbitTemplate rabbitTemplate;

	public void send(NotificationEvent dto) {
		rabbitTemplate.convertAndSend(
			"notification.exchange",           // 교환기 이름
			"user." + dto.getReceiverId(),     // 라우팅 키 - user별 큐
			dto                                 // 메시지 본문
		);
	}
}
