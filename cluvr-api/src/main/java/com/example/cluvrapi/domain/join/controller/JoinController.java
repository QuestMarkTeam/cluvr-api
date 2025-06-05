package com.example.cluvrapi.domain.join.controller;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.cluvrapi.domain.join.dto.request.CreateJoinRequestDto;
import com.example.cluvrapi.domain.join.dto.response.CreateJoinResponseDto;
import com.example.cluvrapi.domain.join.service.JoinService;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

@RestController
@RequiredArgsConstructor
public class JoinController {

	private final JoinService joinService;

	@PostMapping("/clubs/{clubId}/join")
	public ResponseEntity<BaseResponse<CreateJoinResponseDto>> createJoin(
		// @Auth AuthUser authUser,
		@PathVariable Long clubId,
		@Valid @RequestBody CreateJoinRequestDto joinRequestDto
	) {
		CreateJoinResponseDto joinResponseDto = joinService.createJoin(1L, clubId, joinRequestDto);
		return ResponseEntity.ok(BaseResponse.success(joinResponseDto, ResponseCode.CREATED));
	}
}
