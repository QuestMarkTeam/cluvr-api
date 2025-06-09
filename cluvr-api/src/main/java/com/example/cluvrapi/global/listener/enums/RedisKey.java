package com.example.cluvrapi.global.listener.enums;

import lombok.Getter;

@Getter
public enum RedisKey {

	POINT_LOG("user:point"),
	USER_BOARD_LOG("user:board"),

	// HASH 메인 키
	USER_ACTIVITY_COUNT("user:%s:count:%s"), // ex) user:point:count:userId
	// HASH 하위 키
	TOTAL_ANSWER("answer"),
	TOTAL_SELECTED("selected"),
	TOTAL_SCORE("score"),
	TOTAL_QUESTION("question"),
	;

	private final String key;

	RedisKey(String key) {
		this.key = key;
	}

}
