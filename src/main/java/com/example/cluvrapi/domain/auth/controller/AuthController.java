package com.example.cluvrapi.domain.auth.controller;

import static com.example.cluvrapi.global.response.ResponseCode.*;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cluvrapi.domain.auth.dto.request.CompleteProfileRequestDto;
import com.example.cluvrapi.domain.auth.dto.request.LoginUserRequestDto;
import com.example.cluvrapi.domain.auth.dto.request.RefreshTokenDto;
import com.example.cluvrapi.domain.auth.dto.request.SignUpUserRequestDto;
import com.example.cluvrapi.domain.auth.dto.request.SignUpVerifyRequestDto;
import com.example.cluvrapi.domain.auth.dto.response.LoginUserResponseDto;
import com.example.cluvrapi.domain.auth.dto.response.SignUpUserResponseDto;
import com.example.cluvrapi.domain.auth.service.AuthService;
import com.example.cluvrapi.domain.user.dto.response.GetUserMeResponseDto;
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.jwt.JwtUtil;
import com.example.cluvrapi.global.jwt.RefreshTokenService;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
	private final JwtUtil jwtUtil;
	private final RefreshTokenService refreshTokenService;

	@PostMapping("/signup")
	public ResponseEntity<BaseResponse<String>> signUp(
		@Valid @RequestBody SignUpUserRequestDto dto
	) {
		authService.signUp(dto);
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(BaseResponse.success(
				"회원가입이 완료되었습니다.",
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

	@PostMapping("/refresh")
	public ResponseEntity<BaseResponse<LoginUserResponseDto>> refreshToken(
		@RequestBody RefreshTokenDto dto
	) {
		String refreshToken = dto.getRefreshToken();
		
		if (!refreshTokenService.validateRefreshToken(refreshToken)) {
			throw new BusinessException(ResponseCode.TOKEN_INVALID, "유효하지 않은 리프레시 토큰입니다.");
		}
		
		Long userId = jwtUtil.getUserIdFromToken(refreshToken);
		String email = jwtUtil.getEmailFromToken(refreshToken);
		String role = jwtUtil.getUserRoleFromToken(refreshToken);
		
		// 새로운 액세스 토큰과 리프레시 토큰 생성
		String newAccessToken = jwtUtil.generateAccessToken(userId, email, role);
		String newRefreshToken = jwtUtil.generateRefreshToken(userId, email, role);
		
		// 새로운 리프레시 토큰 저장
		refreshTokenService.saveRefreshToken(userId, newRefreshToken);
		
		LoginUserResponseDto responseDto = LoginUserResponseDto.from(
			email,
			"", // name은 필요시 조회
			newAccessToken,
			newRefreshToken,
			null
		);
		
		return ResponseEntity.ok(BaseResponse.success(responseDto, ResponseCode.OK));
	}

	private String resolveBearer(String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			throw new BusinessException(ResponseCode.TOKEN_INVALID);
		}
		return authorizationHeader.substring(7);
	}

	// @Profile({"local", "test"})
	// @PostMapping("/test-signup")
	// public ResponseEntity<BaseResponse<SignUpUserResponseDto>> simpleSignUp(
	// 	@Valid @RequestBody SignUpUserRequestDto dto) {
	// 	SignUpUserResponseDto res = authService.testSignUp(dto);
	// 	return ResponseEntity
	// 		.status(HttpStatus.CREATED)
	// 		.body(BaseResponse.success(res, ResponseCode.CREATED));
	// }
}
