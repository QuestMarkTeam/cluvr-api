package com.example.cluvrapi.domain.gem.handler;

import com.example.cluvrapi.domain.gem.dto.request.UpdateGemRequestDto;
import com.example.cluvrapi.domain.gem.enums.GemActionType;

public interface GemMethodHandler {
	boolean supports(GemActionType type);
	void handle(Long userId, UpdateGemRequestDto dto);
}
