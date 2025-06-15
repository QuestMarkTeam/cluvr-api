package com.example.cluvrnotifications.domain.notification.dto.event;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.cluvrnotifications.domain.notification.enums.NotiTargetType;
import com.example.cluvrnotifications.domain.notification.enums.NotificationType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent implements Serializable {

	//지금은 테스트용 단발성 고정 아이디고 고도화때 역직렬화버전 추가할예정입니다.
	private static final long serialVersionUID = 1L;

	private Long receiverId;
	private NotificationType type;
	private String content;
	private NotiTargetType targetType;
	private Long targetId;

	public static NotificationEvent from(
		Long receiverId,
		NotificationType type,
		String content,
		NotiTargetType targetType,
		Long targetId
	) {
		return new NotificationEvent(receiverId, type, content, targetType, targetId);
	}
}
