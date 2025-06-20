package com.example.cluvrapi.domain.board.listener;

import lombok.RequiredArgsConstructor;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.example.cluvrapi.domain.board.listener.dto.BoardEventRequestDto;
import com.example.cluvrapi.domain.board.service.BoardEvent;
import com.example.cluvrapi.domain.board.service.BoardEventRedisService;

@RequiredArgsConstructor
@Component
public class BoardEventListener {
	private final BoardEventRedisService redisService;

	@Async
	@EventListener
	public void handleUserActivity(BoardEvent event) {
		BoardEventRequestDto eventDto = event.getDto();
		Long userId = event.getUserId();
		String redisKey = event.getRedisKey().buildKey(userId);

		// 포인트 , 게시글 , 카테고리별 활동 기록 카운트 용 레디스 ex) 게시글 작성 + 1
		redisService.incrementHashValue(userId, redisKey, eventDto);
	}
}
