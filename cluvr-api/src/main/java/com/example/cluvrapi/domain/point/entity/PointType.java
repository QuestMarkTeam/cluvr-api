package com.example.cluvrapi.domain.point.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PointType {
	LOGIN(100, 1), COMMENT(10, 5), BOARD(50, 5);

	private final Integer amount; // 포인트 증가량

	private final Integer todayLimit; // 하루 미션 제한

}
