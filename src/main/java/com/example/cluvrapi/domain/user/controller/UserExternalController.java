package com.example.cluvrapi.domain.user.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cluvrapi.domain.user.service.UserQueryService;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserExternalController {

	private final UserQueryService userQueryService;

	@GetMapping("/sub/{sub}/user-id")
	public ResponseEntity<BaseResponse<Long>> getUserIdBySub(@PathVariable String sub) {
		Long userId = userQueryService.getUserIdBySub(sub);
		return ResponseEntity.ok(BaseResponse.success(userId, ResponseCode.OK));
	}
}
