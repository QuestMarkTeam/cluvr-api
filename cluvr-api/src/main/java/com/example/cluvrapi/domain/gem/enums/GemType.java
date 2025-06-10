package com.example.cluvrapi.domain.gem.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GemType {
	// 적립
	LOGIN(100, 1, "로그인 적립", GemFlowType.EARN),
	COMMENT(10, 5, "답변 작성 적립", GemFlowType.EARN),
	BOARD(50, 5, "게시글 작성 적립", GemFlowType.EARN),

	// 사용
	ITEM_PURCHASE(200, 0, "아이템 구매 차감", GemFlowType.SPEND),

	// 소멸 등
	EXPIRED(-1, 0, "포인트 소멸", GemFlowType.EXPIRE);

	private final Integer amount;
	private final Integer todayLimit;
	private final String description;
	private final GemFlowType flowType;

}
