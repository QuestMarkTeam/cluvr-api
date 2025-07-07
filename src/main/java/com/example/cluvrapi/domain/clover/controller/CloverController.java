package com.example.cluvrapi.domain.clover.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cluvrapi.domain.clover.dto.response.FindCloverLogResponseDto;
import com.example.cluvrapi.domain.clover.dto.response.FindCloverResponseDto;
import com.example.cluvrapi.domain.clover.service.CloverService;
import com.example.cluvrapi.domain.common.annotation.Auth;
import com.example.cluvrapi.domain.common.dto.AuthUser;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clovers")
public class CloverController {
	private final CloverService cloverService;

	@GetMapping
	public ResponseEntity<BaseResponse<FindCloverResponseDto>> findCloverByUserId(@Auth AuthUser user) {
		return ResponseEntity.ok(BaseResponse.success(cloverService.findCloverByUserId(user.id()), ResponseCode.OK));
	}

	@GetMapping("/logs")
	public ResponseEntity<BaseResponse<List<FindCloverLogResponseDto>>> findCloverLogByUserId(@Auth AuthUser user) {
		return ResponseEntity.ok(BaseResponse.success(cloverService.findCloverLogByUserId(user.id()), ResponseCode.OK));
	}

}
