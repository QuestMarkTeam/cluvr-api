package com.example.cluvrnotifications.domain.notification.repository.custom;

import java.util.List;

import com.example.cluvrnotifications.domain.notification.entity.NotificationDocument;

/**
 * 설명: MongoDB에 저장된 NotificationDocument에 대해
 * 사용자 ID를 기준으로 조회 및 삭제를 수행하는 커스텀 리포지토리 인터페이스
 *
 * 주로 SSE 연결이 되지 않은 상태에서 수신한 알림을
 * 이후 유저가 접속했을 때 꺼내서 전송하고 삭제하기 위한 용도
 *
 *
 * @author escomputer
 */
public interface NotificationCacheRepositoryCustom {

	/**
	 * 설명: 특정 사용자 ID(receiverId)에 해당하는 모든 NotificationDocument를 조회
	 *
	 * @param receiverId 알림 수신 대상 사용자 ID
	 * @return 해당 사용자의 알림 리스트
	 *
	 * @author escomputer
	 */
	List<NotificationDocument> findAllByReceiverId(Long receiverId);

	void deleteAllById(List<String> ids);
}
