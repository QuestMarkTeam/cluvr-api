package com.example.cluvrapi.domain.rank.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cluvrapi.domain.rank.dto.FindRankLogResponseDto;
import com.example.cluvrapi.domain.rank.dto.FindRankResponseDto;
import com.example.cluvrapi.domain.rank.service.RankService;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ranks")
public class RankController {
	private final RankService rankService;

	@GetMapping
	public ResponseEntity<BaseResponse<FindRankResponseDto>> findRankByUserId() {
		return ResponseEntity.ok(BaseResponse.success(rankService.findRankByUserId(1L), ResponseCode.OK));
	}

	@GetMapping("/logs")
	public ResponseEntity<BaseResponse<FindRankLogResponseDto>> findRankLogByUserId() {
		return ResponseEntity.ok(BaseResponse.success(rankService.findRankLogByUserId(1L), ResponseCode.OK));
	}

}
