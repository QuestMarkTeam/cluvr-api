package com.example.cluvrnotifications.domain.notification.repository.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * 설명: 사용자별 SseEmitter를 메모리에 저장하고 관리하는 저장소 클래스
 *
 * - SSE 연결 시 save()
 * - 알림 전송 시 get()
 * - 연결 끊길 경우 delete()
 *
 *
 * @author escomputer
 */

@Repository
public class SseEmitterRepository {

	private final Map<Long, SseEmitter> emitterMap = new ConcurrentHashMap<>();

	/**
	 * 설명: 사용자 ID 기준으로 emitter를 저장
	 *
	 * <p>{추가적인 설명이 필요하다면 여기에 작성합니다.}
	 *
	 * @param userId 사용자 ID
	 * @param emitter 연결된 SseEmitter
	 *
	 * @author escomputer
	 */

	public void save(Long userId, SseEmitter emitter) {
		emitterMap.put(userId, emitter);
	}

	/**
	 * 설명: 사용자 ID 기준으로 emitter를 조회
	 *
	 * @param userId 사용자 ID
	 * @return 해당 사용자의 emitter 또는 null
	 *
	 * @author escomputer
	 */
	public SseEmitter get(Long userId) {
		return emitterMap.get(userId);
	}

	/**
	 * 설명: 사용자 ID 기준으로 emitter를 삭제
	 *
	 * @param userId 사용자 ID
	 *
	 * @author escomputer
	 */
	public void delete(Long userId) {
		emitterMap.remove(userId);
	}
}
