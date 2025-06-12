package com.example.cluvrapi.domain.clover.listener;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.example.cluvrapi.domain.clover.listener.dto.CloverEventDto;
import com.example.cluvrapi.domain.clover.service.CloverEvent;
import com.example.cluvrapi.domain.clover.service.CloverEventRedisService;
import com.example.cluvrapi.domain.clover.service.CloverService;

@RequiredArgsConstructor
@Component
public class CloverEventListener {
	private final CloverEventRedisService redisService;
	private final CloverService cloverService;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleUserActivity(CloverEvent event) {
		CloverEventDto eventDto = event.getDto();
		Long userId = event.getUserId();
		String redisKey = event.getRedisKey().buildKey(userId);
		double score = System.currentTimeMillis();

		// 이벤트에 해당하는 클로버 적립 로그 저장
		redisService.setZSetValue(redisKey, eventDto, score);
		redisService.setExpireOfDay(redisKey, 31L);
		// 포인트 , 게시글 , 카테고리별 활동 기록 카운트 용 레디스 ex) 게시글 작성 + 1
		// handleUserActivityCount(userId);
	}
}
