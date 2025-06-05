package com.example.cluvrapi.domain.join.service;

import com.example.cluvrapi.domain.join.dto.request.CreateJoinRequestDto;
import com.example.cluvrapi.domain.join.dto.response.CreateJoinResponseDto;

public interface JoinService {
	CreateJoinResponseDto createJoin(Long userId, Long clubId, CreateJoinRequestDto joinRequestDto);
}
