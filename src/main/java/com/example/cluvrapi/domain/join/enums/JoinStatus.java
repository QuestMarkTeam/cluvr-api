package com.example.cluvrapi.domain.join.enums;

/**
 * 가입 신청에서 가입 상태를 정의하는 열거형입니다.
 */

public enum JoinStatus {
	PENDING,    // 요청됨 (아직 승인되지 않음)
	APPROVED,   // 승인됨
	REJECTED   // 거절됨
}
