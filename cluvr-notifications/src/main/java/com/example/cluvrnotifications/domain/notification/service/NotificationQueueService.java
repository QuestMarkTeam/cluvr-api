package com.example.cluvrnotifications.domain.notification.service;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.stereotype.Service;

/**
 * 설명: 사용자별 큐(queue.user.{id})를 동적으로 생성하고 바인딩하는 서비스
 *
 *
 * @author escomputer
 */
@Service
@RequiredArgsConstructor
public class NotificationQueueService {

	private final AmqpAdmin amqpAdmin;
	private final DirectExchange notificationExchange;

	/**
	 * 설명: 사용자 ID 기준으로 큐를 동적으로 생성하고, exchange에 바인딩
	 *
	 * 큐에 메시지는 살아있어야한다(접속 종료, 재시작 등등에도)
	 * 그래서 durable !!
	 *
	 * @param userId 사용자 ID
	 *
	 * @author escomputer
	 */

	public void declareQueueAndBinding(Long userId) {
		String queueName = "user." + userId;

		Queue queue = QueueBuilder.durable(queueName).build(); //영속
		amqpAdmin.declareQueue(queue);

		Binding binding = BindingBuilder
			.bind(queue)
			.to(notificationExchange)
			.with(queueName);//queuename = 라우팅 키

		amqpAdmin.declareBinding(binding);
	}
}
