package com.example.cluvrapi.domain.clover.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cluvrapi.domain.clover.dto.FindCloverLogResponseDto;
import com.example.cluvrapi.domain.clover.dto.FindCloverResponseDto;
import com.example.cluvrapi.domain.clover.service.CloverService;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clovers")
public class CloverController {
	private final CloverService cloverService;

	@GetMapping
	public ResponseEntity<BaseResponse<FindCloverResponseDto>> findCloverByUserId() {
		return ResponseEntity.ok(BaseResponse.success(cloverService.findCloverByUserId(1L), ResponseCode.OK));
	}

	@GetMapping("/logs")
	public ResponseEntity<BaseResponse<List<FindCloverLogResponseDto>>> findCloverLogByUserId() {
		return ResponseEntity.ok(BaseResponse.success(cloverService.findCloverLogByUserId(1L), ResponseCode.OK));
	}

}
