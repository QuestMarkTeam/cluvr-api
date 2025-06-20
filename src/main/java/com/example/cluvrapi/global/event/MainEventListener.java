package com.example.cluvrapi.global.event;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.example.cluvrapi.global.event.service.UserEvent;

@RequiredArgsConstructor
@Component
public class MainEventListener {

	// 고도화 예정 이벤트 재 발행해서 트랜잭션 안정되게
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleUserActivity(UserEvent<?> event) {

	}
}
