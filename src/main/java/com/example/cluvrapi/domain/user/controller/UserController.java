package com.example.cluvrapi.domain.user.controller;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cluvrapi.domain.auth.service.AuthService;
import com.example.cluvrapi.domain.common.annotation.Auth;
import com.example.cluvrapi.domain.common.dto.AuthUser;
import com.example.cluvrapi.domain.user.dto.request.UpdateUserRequestDto;
import com.example.cluvrapi.domain.user.dto.response.GetUserGemResponseDto;
import com.example.cluvrapi.domain.user.dto.response.GetUserMeResponseDto;
import com.example.cluvrapi.domain.user.dto.response.GetUserOtherResponseDto;
import com.example.cluvrapi.domain.user.service.UserService;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final AuthService authService;

	@GetMapping("/me")
	public ResponseEntity<BaseResponse<GetUserMeResponseDto>> getMyProfile(@Auth AuthUser authUser) {
		GetUserMeResponseDto profileDto = userService.getMyProfile(authUser.id());
		return ResponseEntity.ok(BaseResponse.success(profileDto, ResponseCode.OK));
	}

	@GetMapping("/{id}")
	public ResponseEntity<BaseResponse<GetUserOtherResponseDto>> getOtherUserProfile(@Auth AuthUser currentUser,
		@PathVariable("id") Long otherUserId) {

		if (currentUser.id().equals(otherUserId)) {
			return ResponseEntity.badRequest()
				.body(BaseResponse.error(ResponseCode.VALID_FAIL, "본인 프로필 조회 시 /users/me 사용하세요"));
		}

		GetUserOtherResponseDto dto = userService.getOtherUserProfile(otherUserId);
		return ResponseEntity.ok(BaseResponse.success(dto, ResponseCode.OK));
	}

	@GetMapping("/me/gem")
	public ResponseEntity<BaseResponse<GetUserGemResponseDto>> getMyGem(
		@Auth AuthUser authUser) {
		GetUserGemResponseDto dto = userService.getUserGem(authUser.id());
		return ResponseEntity
			.ok(BaseResponse.success(dto, ResponseCode.OK));

	}

	@PutMapping("/me")
	public ResponseEntity<BaseResponse<GetUserMeResponseDto>> updateMyProfile(
		@Auth AuthUser currentUser,
		@Valid @RequestBody UpdateUserRequestDto updateDto
	) {
		GetUserMeResponseDto updatedDto = userService.updateMyProfile(currentUser.id(), updateDto);
		return ResponseEntity
			.ok(BaseResponse.success(updatedDto, ResponseCode.OK));
	}

	@DeleteMapping("/me")
	public ResponseEntity<BaseResponse<String>> deleteMyProfile(@Auth AuthUser authUser) {
		userService.deleteMyProfile(authUser.id());
		return ResponseEntity
			.ok(
				BaseResponse.success(
					"회원 탈퇴가 완료되었습니다.",
					ResponseCode.OK
				)
			);
	}

}
