package com.example.cluvrapi.domain.gem.enums;

import java.time.LocalDateTime;

public enum GemActionType {
	EVENT_EARN(1),   // 적립
	USE(-1),  // 사용
	EXPIRE(-1), // 소멸
	REFUND(1),// 환불
	CHARGE(1)
	;

	private final Integer multiplier;

	GemActionType(Integer multiplier) {
		this.multiplier = multiplier;
	}

	public Integer apply(Integer amount) {
		return amount * multiplier;
	}

	public LocalDateTime getEventDate() {
		return this == EVENT_EARN || this == REFUND ? LocalDateTime.now() : null;
	}

	public LocalDateTime getDeleteDate() {
		return this == USE || this == EXPIRE  ? LocalDateTime.now() : null;
	}
}
