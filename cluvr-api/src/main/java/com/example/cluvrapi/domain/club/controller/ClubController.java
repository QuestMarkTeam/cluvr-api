package com.example.cluvrapi.domain.club.controller;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cluvrapi.domain.club.dto.request.CreateClubRequestDto;
import com.example.cluvrapi.domain.club.dto.request.UpdateClubRequestDto;
import com.example.cluvrapi.domain.club.dto.request.UpgradeMemberCountRequestDto;
import com.example.cluvrapi.domain.club.dto.response.CreateClubResponseDto;
import com.example.cluvrapi.domain.club.dto.response.FindAllClubResponseDto;
import com.example.cluvrapi.domain.club.dto.response.FindClubResponseDto;
import com.example.cluvrapi.domain.club.enums.ClubType;
import com.example.cluvrapi.domain.club.service.ClubService;
import com.example.cluvrapi.domain.common.annotation.Auth;
import com.example.cluvrapi.domain.common.dto.AuthUser;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

@RestController
@RequestMapping("/clubs")
@RequiredArgsConstructor
public class ClubController {

	private final ClubService clubService;

	@PostMapping
	public ResponseEntity<BaseResponse<CreateClubResponseDto>> createClub(
		// @Auth AuthUser authUser,
		@Valid @RequestBody CreateClubRequestDto createClubRequestDto
	) {
		CreateClubResponseDto dto = clubService.createClub(1L, createClubRequestDto);
		return ResponseEntity.ok(BaseResponse.success(dto, ResponseCode.CREATED));
	}

	@GetMapping("/{clubId}")
	public ResponseEntity<BaseResponse<FindClubResponseDto>> findClubById(
		@PathVariable Long clubId
	) {
		FindClubResponseDto dto = clubService.findClubById(clubId);
		return ResponseEntity.ok(BaseResponse.success(dto, ResponseCode.OK));
	}

	@GetMapping
	public ResponseEntity<BaseResponse<PageResponseDto<FindAllClubResponseDto>>> findAllClub(
		@RequestParam(defaultValue = "STUDY") ClubType clubType,
		@PageableDefault(size = 5, sort = "createdAt") Pageable pageable
	) {
		PageResponseDto<FindAllClubResponseDto> dto = clubService.findAllClub(clubType, pageable);
		return ResponseEntity.ok(BaseResponse.success(dto, ResponseCode.OK));
	}

	@PatchMapping("/{clubId}")
	public ResponseEntity<BaseResponse<Void>> updateClub(
		@PathVariable Long clubId,
		@Valid @RequestBody UpdateClubRequestDto updateClubRequest
	) {
		clubService.updateClub(clubId, updateClubRequest);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.OK));
	}

	@DeleteMapping("/{clubId}")
	public ResponseEntity<BaseResponse<Void>> deleteClub(
		@PathVariable Long clubId
	) {
		clubService.deleteClub(clubId);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.OK));
	}

	@PatchMapping("/{clubId}/member-count/upgrade")
	/**
	 * 설명: 클럽의 최대 인원수를 증가시킵니다. (포인트 미사용)
	 *
	 * @param authUser 인증된 사용자 정보
	 * @param clubId   최대 인원수를 증가시킬 클럽의 고유 식별자
	 * @return 성공 응답 반환 (본문 없음, 상태 코드 204 NO_CONTENT)
	 * @author sinyoung0403
	 */
	public ResponseEntity<BaseResponse<Void>> upgradeMemberCount(
		@Auth AuthUser authUser,
		@PathVariable("clubId") Long clubId,
		@Valid @RequestBody UpgradeMemberCountRequestDto memberCountRequestDto
	) {
		clubService.upgradeMemberCount(authUser.id(), clubId, memberCountRequestDto);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(BaseResponse.success(ResponseCode.NO_CONTENT));
	}

	@PatchMapping("/{clubId}/member-count/upgrade-with-gems")
	/**
	 * 설명: 클럽의 최대 인원수를 증가시킵니다. (Gem 사용)
	 *
	 * @param authUser 인증된 사용자 정보
	 * @param clubId   최대 인원수를 증가시킬 클럽의 고유 식별자
	 * @return 성공 응답 반환 (본문 없음, 상태 코드 204 NO_CONTENT)
	 * @author sinyoung0403
	 */
	public ResponseEntity<BaseResponse<Void>> upgradeMemberCountWithGem(
		@Auth AuthUser authUser,
		@PathVariable("clubId") Long clubId
	) {
		System.out.println(authUser.id());
		clubService.upgradeMemberCountWithGem(authUser.id(), clubId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(BaseResponse.success(ResponseCode.NO_CONTENT));
	}
}
