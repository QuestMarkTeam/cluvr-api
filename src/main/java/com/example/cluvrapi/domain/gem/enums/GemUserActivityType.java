package com.example.cluvrapi.domain.gem.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GemUserActivityType { // 포인트가 어디에서 어떤 활동으로 적립인지 사용인지 구분해주는 enum
	// 적립
	LOGIN(100, 1, "로그인 적립", GemActionType.EVENT_EARN),
	COMMENT(10, 5, "답변 작성 적립", GemActionType.EVENT_EARN),
	BOARD(50, 5, "게시글 작성 적립", GemActionType.EVENT_EARN),
	// 소모
	CLUB_CREATE(200, 0, "클럽 생성", GemActionType.USE),

	// 사용
	ITEM_PURCHASE(0, 0, "아이템 구매 차감", GemActionType.USE),
	CHAT_CREATE(0, 0, "채팅방 생성", GemActionType.CHAT_USE),


	// 소멸 등
	EXPIRED(0, 0, "포인트 소멸", GemActionType.EXPIRE),

	// 충전
	CHARGE(0, 0, "포인트 충전", GemActionType.CHARGE)
	;

	private final Integer gem;
	private final Integer todayLimit;
	private final String description;
	private final GemActionType flowType;

}
