package com.example.cluvrnotifications.global.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

	public static final String EXCHANGE_NAME = "notification.exchange";
	public static final String QUEUE_NAME = "user.1";  // 우선 테스트용으로 1번 유저 (정적으로 바인딩함)

	@Bean
	public DirectExchange notificationExchange() {
		return new DirectExchange(EXCHANGE_NAME);
	}

	@Bean
	public Queue userQueue() {
		return new Queue(QUEUE_NAME, true); // durable = true
	}

	@Bean
	public Binding binding(Queue userQueue, DirectExchange notificationExchange) {
		return BindingBuilder.bind(userQueue)
			.to(notificationExchange)
			.with(QUEUE_NAME); // 라우팅 키 = queueName
	}

	@Bean
	public AmqpAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
		return new RabbitAdmin(connectionFactory);
	}
}
