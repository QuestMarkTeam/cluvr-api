package com.example.cluvrapi.domain.clubMember.controller;

import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RestController;

import com.example.cluvrapi.domain.clubMember.dto.request.ChangeMemberRoleRequestDto;
import com.example.cluvrapi.domain.clubMember.dto.request.HandleJoinStatusRequestDto;
import com.example.cluvrapi.domain.clubMember.dto.response.ClubMemberInfoResponseDto;
import com.example.cluvrapi.domain.clubMember.dto.response.GetMemberRoleResponseDto;
import com.example.cluvrapi.domain.clubMember.service.ClubMemberService;
import com.example.cluvrapi.domain.common.annotation.Auth;
import com.example.cluvrapi.domain.common.dto.AuthUser;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/clubs/{clubId}")
@RequiredArgsConstructor

public class ClubMemberController {

	private final ClubMemberService clubMemberService;

	@PostMapping("/join-requests/{joinRequestId}/status")
	public ResponseEntity<BaseResponse<Void>> handleJoinStatus(
		@PathVariable Long clubId,
		@PathVariable Long joinRequestId,
		@RequestBody HandleJoinStatusRequestDto dto,
		@Auth AuthUser authUser
	) {
		clubMemberService.handleJoinRequest(clubId, joinRequestId, dto.getStatus(), authUser);
		return ResponseEntity
			.ok(BaseResponse.success(ResponseCode.OK));
	}

	@PatchMapping("/members/{memberId}/role")
	public ResponseEntity<BaseResponse<Void>> changeRole(
		@PathVariable Long clubId,
		@PathVariable("memberId") Long targetMemberId,
		@RequestBody @Valid ChangeMemberRoleRequestDto dto,
		@Auth AuthUser authUser
	) {
		clubMemberService.changeMemberRole(clubId, authUser, targetMemberId, dto.getNewRole());
		return ResponseEntity
			.ok(BaseResponse.success(ResponseCode.OK));
	}

	@DeleteMapping("/members/me")
	public ResponseEntity<BaseResponse<Void>> withdrawFromClub(
		@PathVariable Long clubId,
		@Auth AuthUser authUser
	) {
		clubMemberService.withdrawFromClub(clubId, authUser);
		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.body(BaseResponse.success(ResponseCode.NO_CONTENT));
	}

	@DeleteMapping("/members/{memberId}")
	public ResponseEntity<BaseResponse<Void>> kickMember(
		@PathVariable Long clubId,
		@PathVariable Long memberId,
		@Auth AuthUser authUser
	) {
		clubMemberService.kickMember(clubId, authUser, memberId);
		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.body(BaseResponse.success(ResponseCode.NO_CONTENT));
	}

	@GetMapping("/members")
	public ResponseEntity<BaseResponse<Page<ClubMemberInfoResponseDto>>> listMembers(
		@PathVariable Long clubId,
		@Auth AuthUser authUser,
		@PageableDefault(size = 20) Pageable pageable
	) {
		Page<ClubMemberInfoResponseDto> page = clubMemberService.listMembers(clubId, authUser, pageable);
		return ResponseEntity
			.ok(BaseResponse.success(page, ResponseCode.OK));
	}

	@GetMapping("/members/{userId}/role")
	public ResponseEntity<BaseResponse<GetMemberRoleResponseDto>> getMemberRole(
		@PathVariable Long clubId,
		@PathVariable("userId") Long targetUserId,
		@Auth AuthUser authUser
	) {
		GetMemberRoleResponseDto dto = clubMemberService.getMemberRole(clubId, targetUserId);
		return ResponseEntity
			.ok(BaseResponse.success(dto, ResponseCode.OK));
	}

	@PostMapping("/{targetMemberId}/transfer-owner")

	public BaseResponse<Void> transferOwner(
		@PathVariable Long clubId,
		@PathVariable Long targetMemberId,
		@Auth AuthUser authUser
	) {
		clubMemberService.changeOwnership(clubId, authUser, targetMemberId);
		return BaseResponse.success(null, ResponseCode.OK);
	}

}
