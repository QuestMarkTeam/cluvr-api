package com.example.cluvrapi.domain.notification.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cluvrapi.domain.notification.enums.NotiTargetType;
import com.example.cluvrapi.domain.notification.enums.NotificationType;
import com.example.cluvrapi.domain.notification.event.NotificationEvent;
import com.example.cluvrapi.domain.notification.event.NotificationProducer;

/**
 * 설명:
 * 알림 발행이 무사히 되는지 테스트용입니다.
 *나중에 삭제할 예정입니다.
 *
 * @author escomputer
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class TestController {

	private final NotificationProducer notificationProducer;

	@PostMapping("/notify")
	public ResponseEntity<String> testNotify(@RequestParam Long userId) {
		NotificationEvent event = NotificationEvent.from(
			userId,
			NotificationType.COMMENT,
			"테스트 알림 도착! 🎉",
			NotiTargetType.USER,
			99L
		);
		notificationProducer.send(event);
		return ResponseEntity.ok("✅ 알림 발행 완료");
	}
}
