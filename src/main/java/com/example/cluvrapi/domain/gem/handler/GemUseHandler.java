package com.example.cluvrapi.domain.gem.handler;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import com.example.cluvrapi.domain.gem.dto.request.UpdateGemRequestDto;
import com.example.cluvrapi.domain.gem.enums.GemActionType;
import com.example.cluvrapi.domain.gem.enums.GemUserActivityType;
import com.example.cluvrapi.domain.gem.service.GemService;

@Component
@RequiredArgsConstructor
public class GemUseHandler implements GemMethodHandler { // 이벤트 적립 처리

	private final GemService gemService; // 내부에서 트랜잭션 공유 가능하도록

	@Override
	public boolean supports(GemActionType type) {
		return GemActionType.USE.equals(type);
	}

	@Override
	public void handle(Long userId, UpdateGemRequestDto dto) {
		if (dto.getGemUserActivityType() != GemUserActivityType.CHAT_CREATE) {
			gemService.useGem(userId,dto);
		}
	}

}
