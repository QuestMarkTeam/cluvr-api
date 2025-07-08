package com.example.cluvrapi.domain.notification.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.example.cluvrapi.global.client.NotificationQueueClient;
import com.example.cluvrapi.global.config.RabbitConfig;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationProducer {

	private final RabbitTemplate rabbitTemplate;
	private final NotificationQueueClient queueClient;

	public void send(NotificationEvent dto) {
		Long receiverId = dto.getReceiverId();
		try {
			queueClient.initQueueIfNecessary(receiverId);

			rabbitTemplate.convertAndSend(
				RabbitConfig.EXCHANGE_NAME,           // 교환기 이름
				"user." + dto.getReceiverId(),     // 라우팅 키 - user별 큐
				dto                                 // 메시지 본문
			);
		} catch (AmqpException e) {
			//현재는 로그만 찍어서, 로그 추적 수동조치하게끔 했지만, 추후 고도화 작업때
			//@TransactionalListener or Outbox or Kafka 도입예정
			log.error("RabbitMQ 전송 실패. 사용자 알림 유실 가능: {}", e.getMessage());
		}
	}
}
