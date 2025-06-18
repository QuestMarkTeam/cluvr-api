package com.example.cluvrapi.domain.board.service;

import com.example.cluvrapi.domain.board.listener.dto.BoardEventRequestDto;
import com.example.cluvrapi.domain.clover.enums.Tier;
import com.example.cluvrapi.global.event.enums.RedisKey;
import com.example.cluvrapi.global.event.enums.UserEventType;
import com.example.cluvrapi.global.event.service.UserEvent;

public class BoardEvent extends UserEvent<BoardEventRequestDto> {
	public BoardEvent(Long userId, RedisKey redisKey, UserEventType type, BoardEventRequestDto eventData) {
		super(userId, redisKey, type, eventData);
	}

	public static BoardEvent createEvent(Long userId, Integer totalAnswer, Integer totalClover, Integer totalSelected,
		Integer totalQuestion, Tier tier) {
		return new BoardEvent(userId, RedisKey.BOARD_ACTIVITY_LOG, UserEventType.USER_BOARD,
			BoardEventRequestDto.of(userId, totalAnswer, totalClover, totalSelected, totalQuestion, tier)
		);
	}
}
