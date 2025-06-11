package com.example.cluvrnotifications.domain.notification.event;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.cluvrnotifications.domain.notification.dto.event.NotificationEvent;
import com.example.cluvrnotifications.domain.notification.service.NotificationReceiveService;

/**
 * 설명: RabbitMQ로부터 NotificationEvent 메시지를 수신하는 Consumer 클래스
 *
 * 현재는 정적으로 user.1 큐에 연결되어 있으며, 향후 동적 큐로 확장될 예정
 *
 * @author escomputer
 */

@Component
@RequiredArgsConstructor
public class NotificationConsumer {

	private final NotificationReceiveService notificationReceiveService;

	/**
	 * 설명: MQ에서 수신한 NotificationEvent를 처리
	 *
	 *
	 * @param event 수신된 알림 이벤트
	 * @author escomputer
	 */

	@RabbitListener(queues = "user.1") // 나중에 동적으로 바꿔줄 예정
	public void handle(NotificationEvent event) {
		System.out.println(" 알림 이벤트 수신: " + event.getContent());
		notificationReceiveService.receive(event);
	}

}
