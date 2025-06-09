package com.example.cluvrapi.global.listener.enums;

import lombok.Getter;

@Getter
public enum RedisKey {

	// 로그 키 여기에 날짜도 추가하면 원하는 기간 별 조회도 가능
	GEM_LOG("user:gem:log"),
	CLOVER_LOG("user:clover:log"),
	BOARD_ACTIVITY_LOG("user:board:activity:log"),

	// HASH 메인 키
	USER_ACTIVITY_COUNT("user:activity:count:userId:"), // ex) user:gem:count:userId
	// HASH 하위 키
	TOTAL_ANSWER("answer"),
	TOTAL_SELECTED("selected"),
	TOTAL_SCORE("score"),
	TOTAL_QUESTION("question"),
	TOTAL_GEM("gem"),
	;

	private final String key;

	RedisKey(String key) {
		this.key = key;
	}

}
