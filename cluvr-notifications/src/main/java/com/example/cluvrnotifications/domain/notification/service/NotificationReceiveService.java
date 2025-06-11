package com.example.cluvrnotifications.domain.notification.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.example.cluvrnotifications.domain.notification.dto.event.NotificationEvent;

@Service
@RequiredArgsConstructor
public class NotificationReceiveService {

	public void receive(NotificationEvent event) {
		// 일단 로그만 처리, 추후에 sse연결 여부 판단후 알림 전송 및 mongoDB에 저장하는 로직작성예정
		System.out.println(" 알림 수신 처리: " + event.getType() + " - " + event.getContent());

		// Step 2부터 여기에서 SSE 연결 여부 판단하고,
		//  연결됐으면 바로 보내고, 안됐으면 MongoDB에 저장할 예정
	}
}
