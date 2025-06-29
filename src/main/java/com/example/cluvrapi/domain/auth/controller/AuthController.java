package com.example.cluvrapi.domain.auth.controller;

import static com.example.cluvrapi.global.response.ResponseCode.CREATED;
import static com.example.cluvrapi.global.response.ResponseCode.OK;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cluvrapi.domain.auth.dto.request.LoginUserRequestDto;
import com.example.cluvrapi.domain.auth.dto.request.RefreshTokenDto;
import com.example.cluvrapi.domain.auth.dto.request.SignUpUserRequestDto;
import com.example.cluvrapi.domain.auth.dto.request.SignUpVerifyRequestDto;
import com.example.cluvrapi.domain.auth.dto.response.LoginUserResponseDto;
import com.example.cluvrapi.domain.auth.dto.response.SignUpUserResponseDto;
import com.example.cluvrapi.domain.auth.service.AuthService;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<BaseResponse<String>> signUp(
		@Valid @RequestBody SignUpUserRequestDto dto
	) {
		authService.signUp(dto);
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(BaseResponse.success(
				"회원가입 요청이 성공적으로 전송되었습니다. 이메일 인증을 진행해주세요.",
				CREATED
			));
	}

	@PostMapping("/login")
	public ResponseEntity<BaseResponse<LoginUserResponseDto>> login(
		@Valid @RequestBody LoginUserRequestDto loginUserRequestDto) {
		LoginUserResponseDto responseDto = authService.login(loginUserRequestDto);
		return ResponseEntity.ok(BaseResponse.success(responseDto, ResponseCode.OK));
	}

	@PostMapping("/logout")
	public ResponseEntity<BaseResponse<String>> logout(
		@RequestBody RefreshTokenDto dto
	) {
		authService.logout(dto.getRefreshToken());
		return ResponseEntity.ok(BaseResponse.success("로그아웃 되었습니다.", OK));
	}

	private String resolveBearer(String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			throw new BusinessException(ResponseCode.TOKEN_INVALID);
		}
		return authorizationHeader.substring(7);
	}

	@PostMapping("/verify")
	public ResponseEntity<BaseResponse<String>> verifyEmail(
		@Valid @RequestBody SignUpVerifyRequestDto req
	) {
		authService.completeSignUp(req);
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(BaseResponse.success(
				"이메일 인증이 완료되었습니다.",
				CREATED
			));
	}


	@PostMapping("/test-signup")
	public ResponseEntity<BaseResponse<SignUpUserResponseDto>> simpleSignUp(
		@Valid @RequestBody SignUpUserRequestDto dto) {
		SignUpUserResponseDto res = authService.testSignUp(dto);
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(BaseResponse.success(res, ResponseCode.CREATED));
	}
}
