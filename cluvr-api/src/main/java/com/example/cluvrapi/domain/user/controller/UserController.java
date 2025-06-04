package com.example.cluvrapi.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cluvrapi.domain.common.annotation.Auth;
import com.example.cluvrapi.domain.user.dto.request.LoginUserRequestDto;
import com.example.cluvrapi.domain.user.dto.request.SignUpUserRequestDto;
import com.example.cluvrapi.domain.user.dto.response.GetUserMeResponseDto;
import com.example.cluvrapi.domain.user.dto.response.LoginUserResponseDto;
import com.example.cluvrapi.domain.user.dto.response.SignUpUserResponseDto;
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.service.UserService;
import com.example.cluvrapi.global.jwt.JwtUtil;
import com.example.cluvrapi.global.jwt.RefreshTokenServiceImpl;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final RefreshTokenServiceImpl refreshTokenService;
	private final JwtUtil jwtUtil;

	@PostMapping("/signup")
	public ResponseEntity<BaseResponse<SignUpUserResponseDto>> signUp(
		@Valid @RequestBody SignUpUserRequestDto signUpUserRequestDto
	) {
		SignUpUserResponseDto responseDto = userService.signUp(signUpUserRequestDto);
		return ResponseEntity.ok(BaseResponse.success(responseDto, ResponseCode.CREATED));
	}

	@PostMapping("/login")
	public ResponseEntity<BaseResponse<LoginUserResponseDto>> login(
		@Valid @RequestBody LoginUserRequestDto loginUserRequestDto
	) {
		LoginUserResponseDto responseDto = userService.login(loginUserRequestDto);
		return ResponseEntity.ok(BaseResponse.success(responseDto, ResponseCode.OK));
	}

	@PostMapping("/logout")
	public ResponseEntity<BaseResponse<String>> logout(
		@RequestHeader("Authorization") String authorizationHeader
	) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return ResponseEntity
				.badRequest()
				.body(BaseResponse.error(ResponseCode.TOKEN_INVALID, "유효한 Access Token이 헤더에 없습니다."));
		}

		String accessToken = authorizationHeader.substring(7);

		if (!jwtUtil.validateToken(accessToken)) {
			return ResponseEntity
				.status(401)
				.body(BaseResponse.error(ResponseCode.TOKEN_INVALID, "유효하지 않은 Access Token입니다."));
		}

		long expireMillis = jwtUtil.getExpirationMillis(accessToken);

		long remainingMillis = expireMillis - System.currentTimeMillis();

		refreshTokenService.blacklistAccessToken(accessToken, remainingMillis);

		return ResponseEntity.ok(BaseResponse.success("로그아웃 되어 액세스토큰이 즉시 무효화되었습니다.", ResponseCode.OK));
	}

	@GetMapping("/me")
	public ResponseEntity<BaseResponse<GetUserMeResponseDto>> getMyProfile(@Auth User user) {
		GetUserMeResponseDto profileDto = userService.getMyProfile(user.getId());
		return ResponseEntity.ok(BaseResponse.success(profileDto, ResponseCode.OK));
	}

}
