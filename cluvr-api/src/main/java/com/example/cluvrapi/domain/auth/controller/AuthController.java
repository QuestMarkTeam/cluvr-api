package com.example.cluvrapi.domain.auth.controller;

import static com.example.cluvrapi.global.response.ResponseCode.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cluvrapi.domain.auth.dto.request.LoginUserRequestDto;
import com.example.cluvrapi.domain.auth.dto.request.SignUpUserRequestDto;
import com.example.cluvrapi.domain.auth.dto.response.LoginUserResponseDto;
import com.example.cluvrapi.domain.auth.dto.response.SignUpUserResponseDto;
import com.example.cluvrapi.domain.auth.service.AuthService;
import com.example.cluvrapi.domain.common.annotation.Auth;
import com.example.cluvrapi.domain.common.dto.AuthUser;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<BaseResponse<SignUpUserResponseDto>> signUp(
		@Valid @RequestBody SignUpUserRequestDto signUpUserRequestDto) {
		SignUpUserResponseDto responseDto = authService.signUp(signUpUserRequestDto);
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(BaseResponse.success(responseDto, CREATED));
	}

	@PostMapping("/login")
	public ResponseEntity<BaseResponse<LoginUserResponseDto>> login(
		@Valid @RequestBody LoginUserRequestDto loginUserRequestDto) {
		LoginUserResponseDto responseDto = authService.login(loginUserRequestDto);
		return ResponseEntity.ok(BaseResponse.success(responseDto, ResponseCode.OK));
	}

	@PostMapping("/logout")
	public ResponseEntity<BaseResponse<String>> logout(
		@Auth AuthUser authUser,
		@RequestHeader("Authorization") String authorizationHeader
	) {
		String accessToken = resolveBearer(authorizationHeader);
		authService.logout(authUser.id(), accessToken);
		return ResponseEntity.ok(BaseResponse.success("로그아웃 되었습니다.", OK));
	}

	private String resolveBearer(String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			throw new BusinessException(ResponseCode.TOKEN_INVALID);
		}
		return authorizationHeader.substring(7);
	}
}
