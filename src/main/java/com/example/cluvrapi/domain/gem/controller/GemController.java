package com.example.cluvrapi.domain.gem.controller;

import java.util.List;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cluvrapi.domain.common.annotation.Auth;
import com.example.cluvrapi.domain.common.dto.AuthUser;
import com.example.cluvrapi.domain.gem.dto.request.UpdateGemRequestDto;
import com.example.cluvrapi.domain.gem.dto.response.FindGemLogResponseDto;
import com.example.cluvrapi.domain.gem.dto.response.UpdateGemResponseDto;
import com.example.cluvrapi.domain.gem.service.GemService;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

@RestController
@RequestMapping("/api/gems")
@RequiredArgsConstructor
public class GemController {

	private final GemService gemService;

	@GetMapping("/logs")
	public ResponseEntity<BaseResponse<List<FindGemLogResponseDto>>> findGemLogByUserId(@Auth AuthUser user) {
		return ResponseEntity.ok(BaseResponse.success(gemService.findGemLogByUserId(user.id()), ResponseCode.OK));
	}
}
