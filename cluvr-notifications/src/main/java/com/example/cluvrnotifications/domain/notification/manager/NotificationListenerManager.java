package com.example.cluvrnotifications.domain.notification.manager;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.stereotype.Service;

import com.example.cluvrnotifications.domain.notification.dto.event.NotificationEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 설명: 사용자별 큐(queue.user.{id})를 동적으로 생성하고 바인딩하는 서비스
 *
 *
 * @author escomputer
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationListenerManager {

	private final AmqpAdmin amqpAdmin;
	private final DirectExchange notificationExchange;
	private final ConnectionFactory connectionFactory;
	private final ObjectMapper objectMapper;

	private final Map<Long, SimpleMessageListenerContainer> listenerContainers = new ConcurrentHashMap<>();

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

	public void start(Long userId) {

		//중복생성방지
		if (listenerContainers.containsKey(userId)) {
			log.info("이미 존재하는 컨테이너 있음: user.{}", userId);
			return;
		}

		String queueName = "user." + userId;

		// 큐 생성 & 바인딩
		Queue queue = QueueBuilder.durable(queueName).build(); //영속
		amqpAdmin.declareQueue(queue);

		Binding binding = BindingBuilder
			.bind(queue)
			.to(notificationExchange)
			.with(queueName);//queuename = 라우팅 키

		amqpAdmin.declareBinding(binding);

		//리스너 컨테이너 생성 (동적으로 생성되는 큐를 리스닝함)
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queueName);
		container.setMessageListener((MessageListener)message -> {
			try {
				String raw = new String(message.getBody(), StandardCharsets.UTF_8);
				NotificationEvent event = objectMapper.readValue(raw, NotificationEvent.class);
				log.info("{} 알림 수신 : {} ", queueName, event.getContent());
			} catch (Exception e) {
				log.error("메시지 파싱 실패", e);
			}
		});

		//컨테이너 실행
		container.start();

		//중단하거나 제거하기 위해서 메모리에 저장함
		listenerContainers.put(userId, container);
	}

	public void stop(Long userId) {
		SimpleMessageListenerContainer container = listenerContainers.remove(userId);
		if (container != null) {
			container.stop();
			log.info("user.{} 컨테이너 중단", userId);
		}
	}

	/**
	 * 설명: 큐 삭제
	 *
	 * <p>만약에 사용자가 탈퇴하면 미사용 큐가 되므로 자원낭비 방지를 위하여
	 * 이건 고도화때 사용할 메서드입니다.
	 *
	 * @author escomputer
	 */

	public void remove(Long userId) {
		String queueName = "user." + userId;
		amqpAdmin.deleteQueue(queueName);
		log.info("user.{} 큐 삭제 완료", userId);
	}
}
