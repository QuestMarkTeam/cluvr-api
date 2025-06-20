package com.example.cluvrapi.domain.clover.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CloverUserActivityType { // 내공이 어디에서 어떤 활동으로 적립인지 사용인지 구분해주는 enum

	// 적립
	ACCEPTED_ANSWER("답변 채택 보상", CloverActionType.EARN),
	// 사용
	CREATE_QUESTION("질문 등록 내공 사용", CloverActionType.USE);
	private final String description;
	private final CloverActionType flowType;

}
