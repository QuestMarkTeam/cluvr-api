package com.example.cluvrapi.domain.analytics.controller;

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

	@GetMapping("/test")
	@Transactional
	public void test() {
		publisher.publishEvent(
			BoardEvent.createEvent(1L, 0, 50, 0, 1, Tier.CLOVER1)
		);

	}
}
