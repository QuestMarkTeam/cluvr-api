package com.example.cluvrnotifications.domain.notification.service;

import java.io.IOException;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.cluvrnotifications.domain.notification.dto.event.NotificationEvent;
import com.example.cluvrnotifications.domain.notification.entity.NotificationDocument;
import com.example.cluvrnotifications.domain.notification.repository.base.NotificationCacheRepository;
import com.example.cluvrnotifications.domain.notification.repository.support.SseEmitterRepository;

/**
 * 설명: SSE연결 처리 + MongoDB에서 알림 꺼내서 전송하는 서비스
 *
 * @author escomputer
 */

@Service
@RequiredArgsConstructor
public class NotificationStreamService {

	private static final Long TIMEOUT = 60 * 1000L; //60초 타임아웃

	private final SseEmitterRepository sseEmitterRepository;
	private final NotificationCacheRepository notificationCacheRepository;
	private final NotificationQueueService notificationQueueService;

	/**
	 * 설명: SSE 연결을 생성하고, 기존 MongoDB 알림을 꺼내서 전송 후 삭제
	 *
	 * @param userId 사용자 ID
	 * @return SseEmitter 객체
	 *
	 * @author escomputer
	 */
	public SseEmitter connect(Long userId) {
		//로그인 시점에 큐를 자동으로 생성하고 바인딩함.
		notificationQueueService.declareQueueAndBinding(userId);

		SseEmitter emitter = new SseEmitter(TIMEOUT);
		sseEmitterRepository.save(userId, emitter);

		//MongoDB에 저장된 알림 꺼내기
		List<NotificationDocument> cachedList = notificationCacheRepository.findAllByReceiverId(userId);

		cachedList.forEach(doc -> {
			try {
				emitter.send(SseEmitter.event()
					.name("notification")
					.data(NotificationEvent.from(
						doc.getReceiverId(),
						doc.getNotificationType(),
						doc.getContent(),
						doc.getTargetType(),
						doc.getTargetId()
					)));
			} catch (IOException e) {
				sseEmitterRepository.delete(userId);
			}
		});

		//MongoDB에서 다 꺼냈으니 알림 삭제
		notificationCacheRepository.deleteAllByReceiverId(userId);

		return emitter;
	}
}
