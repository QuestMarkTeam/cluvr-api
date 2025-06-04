package com.example.cluvrapi.domain.point.controller;

import java.util.List;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cluvrapi.domain.point.dto.request.CreatePointRequestDto;
import com.example.cluvrapi.domain.point.dto.response.CreatePointResponseDto;
import com.example.cluvrapi.domain.point.dto.response.FindPointLogResponseDto;
import com.example.cluvrapi.domain.point.service.PointService;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

@RestController
@RequestMapping("/points")
@RequiredArgsConstructor
public class PointController {

	private final PointService pointService;

	@PostMapping
	public ResponseEntity<BaseResponse<CreatePointResponseDto>> chargePoint(
		@Valid @RequestBody CreatePointRequestDto amount) {
		Long userId = 1L;
		return ResponseEntity.ok(BaseResponse.success(pointService.chargePoint(userId, amount), ResponseCode.OK));
	}

	@GetMapping
	public ResponseEntity<BaseResponse<List<FindPointLogResponseDto>>> findPointLogByUserId() {
		Long userId = 1L;
		return ResponseEntity.ok(BaseResponse.success(pointService.findPointLogByUserId(userId), ResponseCode.OK));
	}
}
