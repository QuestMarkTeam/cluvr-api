package com.example.cluvrapi.domain.gem.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import com.example.cluvrapi.domain.gem.dto.request.UpdateGemRequestDto;
import com.example.cluvrapi.domain.gem.enums.GemActionType;
import com.example.cluvrapi.domain.gem.service.GemService;

@Slf4j
@Component
@RequiredArgsConstructor
public class GemChatUseHandler implements GemMethodHandler { // 이벤트 적립 처리

	private final GemService gemService; // 내부에서 트랜잭션 공유 가능하도록

	@Override
	public boolean supports(GemActionType type) {
		return GemActionType.CHAT_USE.equals(type);
	}

	@Override
	public void handle(Long userId, UpdateGemRequestDto dto) {
		log.info("chat api gem 호출");
	}

}
