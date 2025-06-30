package com.example.cluvrapi.domain.gem.controller;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cluvrapi.domain.common.annotation.Auth;
import com.example.cluvrapi.domain.common.dto.AuthUser;
import com.example.cluvrapi.domain.gem.dto.request.UpdateGemRequestDto;
import com.example.cluvrapi.domain.gem.dto.response.UpdateGemResponseDto;
import com.example.cluvrapi.domain.gem.service.GemService;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

@RestController
@RequestMapping("/api/v1/gems")
@RequiredArgsConstructor
public class GemExternalController {

	private final GemService gemService;

	@PostMapping("/use")
	public ResponseEntity<BaseResponse<UpdateGemResponseDto>> useGem(
		 @RequestBody UpdateGemRequestDto amount,
		 @Auth AuthUser authUser
		) {
		return ResponseEntity.ok(BaseResponse.success(gemService.useGem(authUser.id(), amount), ResponseCode.OK));
	}
}
