package com.example.cluvrapi.global.listener;

import java.time.LocalDate;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.example.cluvrapi.global.listener.dto.UserEventDto;
import com.example.cluvrapi.global.listener.enums.UserEventType;
import com.example.cluvrapi.global.listener.service.UserEventRedisService;

@Component
@RequiredArgsConstructor
public class UserActivityEventListener {

	private final UserEventRedisService redisService;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleUserActivity(UserEventDto<?> event) {
		Long userId = event.getUserId();
		UserEventType type = event.getType();
		String today = LocalDate.now().toString();
		String typeName = type.name();
		// String key = type.getPrefix() + ":" + today + ":" + typeName + ":user:" + userId;
		double score = System.currentTimeMillis();

		// 이벤트에 해당하는 유저의 활동 로그 저장
		redisService.setZSetValue("key", event.getDto(), score);
		// 포인트 , 게시글 , 카테고리별 활동 기록 카운트 용 레디스 ex) 게시글 작성 + 1
		handleUserActivityCount(type, userId);
		// 1. 로그 저장 , 2 카운트
	}

	public void handleUserActivityCount(UserEventType type, Long userId) {
		// String key = type.getPrefix() + ":count:" + type.name() + ":user:" + userId;
		redisService.incrementHashValue("key");

	}
}
