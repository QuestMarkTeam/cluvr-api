package com.example.cluvrapi.domain.analytics;

import com.example.cluvrapi.domain.board.listener.dto.BoardEventRequestDto;
import com.example.cluvrapi.domain.board.service.BoardEvent;
import com.example.cluvrapi.domain.clover.enums.Tier;
import com.example.cluvrapi.global.event.enums.RedisKey;
import com.example.cluvrapi.global.event.enums.UserEventType;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import java.util.Random;

@SpringBootTest
public class StatTest {

	@Autowired
	private ApplicationEventPublisher publisher;

	private Random random = new Random();


	@Test
	public void testPublishRandomBoardEvent() {
		for (int i = 0; i < 1; i++) {
			// 랜덤 데이터 생성
			Long userId = (long) (random.nextInt(1000) + 1); // 1~1000 범위
			Integer answer = random.nextInt(10); // 0~9 범위
			Integer clover = random.nextInt(100); // 0~99 범위
			Integer selected = random.nextInt(5); // 0~4 범위
			Integer question = random.nextInt(20); // 0~19 범위
			Tier tier = Tier.values()[random.nextInt(Tier.values().length)]; // 랜덤 티어 선택


			System.out.println("순서 -> " + i);
			// 이벤트 발행
			BoardEvent.createEvent(userId, answer, clover, selected, question, Tier.CLOVER1);
			// 결과 확인용 로그 (Optional)
			System.out.println("Event Published: ");
		}
	}
}
