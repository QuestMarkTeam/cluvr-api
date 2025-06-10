package com.example.cluvrapi.global.listener;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.example.cluvrapi.domain.user.repository.UserRepository;
import com.example.cluvrapi.global.listener.dto.UserEventDto;
import com.example.cluvrapi.global.listener.service.UserEventRedisService;

@Component
@RequiredArgsConstructor
public class UserActivityEventListener {

	private final UserEventRedisService redisService;
	private final UserRepository userRepository;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleUserActivity(UserEventDto<?> event) {
		Long userId = event.getUserId();
		String redisKey = event.getRedisKey().buildKey(userId);
		double score = System.currentTimeMillis();

		// 이벤트에 해당하는 유저의 활동 로그 저장
		redisService.setZSetValue(redisKey, event.getDto(), score);
		// 포인트 , 게시글 , 카테고리별 활동 기록 카운트 용 레디스 ex) 게시글 작성 + 1
		// handleUserActivityCount(userId);
	}

	// public void handleUserActivityCount(Long userId) {
	// 	String key = USER_ACTIVITY_COUNT.getKey() + userId;
	// 	User user = userRepository.findByIdOrElseThrow(userId);
	// 	redisService.incrementHashValue(key);
	// }

	// public void addGem() {
	// 	redisService.incrementHashValue("key");
	// }
}
