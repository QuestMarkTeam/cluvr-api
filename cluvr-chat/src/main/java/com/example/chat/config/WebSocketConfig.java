package com.example.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// 클라이언트가 연결할 엔드포인트 (SockJS 지원)
		registry.addEndpoint("/cluvr-chat")
			.setAllowedOriginPatterns("*")
			// 나중에 특정 url 요청만 접근하도록 할 때 이부분 변경 : setAllowedOriginPatterns("https://www.cluvr.com")
			// 또는 .setAllowedOriginPatterns("https://cluvr.com", "http://localhost:3000")
			.withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// pub은 클라이언트 -> 서버 전송 prefix
		registry.setApplicationDestinationPrefixes("/chat");

		// sub은 서버 -> 클라이언트 브로드캐스트 prefix
		registry.enableSimpleBroker("/sub");
	}
}
