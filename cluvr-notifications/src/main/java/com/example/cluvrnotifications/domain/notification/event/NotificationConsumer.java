package com.example.cluvrnotifications.domain.notification.event;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.cluvrnotifications.domain.notification.dto.event.NotificationEvent;
import com.example.cluvrnotifications.domain.notification.service.NotificationReceiveService;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {

	private final NotificationReceiveService notificationReceiveService;

	@RabbitListener(queues = "user.1") // 나중에 동적으로 바꿔줄 예정
	public void handle(NotificationEvent event) {
		System.out.println(" 알림 이벤트 수신: " + event.getContent());
		notificationReceiveService.receive(event);
	}

}
