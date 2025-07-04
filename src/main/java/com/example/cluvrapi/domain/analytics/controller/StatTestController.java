package com.example.cluvrapi.domain.analytics.controller;

import java.util.Random;

import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cluvrapi.domain.board.service.BoardEvent;
import com.example.cluvrapi.domain.clover.enums.Tier;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StatTestController {

	private final ApplicationEventPublisher publisher; // 이벤트 발행

	private Random random = new Random();

	@GetMapping("/test")
	@Transactional
	public void test() {
		for (int i = 0; i < 100; i++) {
			// 랜덤 데이터 생성
			Long userId = (long) (random.nextInt(1000) + 1); // 1~1000 범위
			Integer answer = random.nextInt(100); // 0~9 범위
			Integer clover = 1000 + random.nextInt(7001); // 1000 ~ 6000 범위
			Integer selected = random.nextInt(100); // 0~4 범위
			Integer question = random.nextInt(100); // 0~19 범위
			Tier tier = Tier.values()[random.nextInt(Tier.values().length)]; // 랜덤 티어 선택

			System.out.println("순서 -> " + i);
			// 이벤트 발행
			publisher.publishEvent(
				BoardEvent.createEvent(userId, answer, clover, selected, question, Tier.CLOVER1)
			);
			// 결과 확인용 로그 (Optional)
			System.out.println("Event Published: ");
		}

	}
}
